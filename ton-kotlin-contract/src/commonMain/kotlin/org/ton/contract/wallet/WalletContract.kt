package org.ton.contract.wallet

import org.ton.api.pk.PrivateKeyEd25519
import org.ton.block.*
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.exception.CellOverflowException
import org.ton.cell.storeRef
import org.ton.contract.Contract
import org.ton.lite.api.liteserver.LiteServerSendMsgStatus
import org.ton.lite.client.LiteClient
import org.ton.logger.Logger
import org.ton.tlb.constructor.AnyTlbConstructor
import org.ton.tlb.storeTlb

abstract class WalletContract(
    override val liteClient: LiteClient,
    val privateKey: PrivateKeyEd25519,
    override val workchainId: Int = 0
) : Contract {
    val logger: Logger by lazy {
        Logger.println(name, Logger.Level.DEBUG)
    }

    override suspend fun deploy(): LiteServerSendMsgStatus {
        val initMessage = createExternalInitMessage()
        logger.info { "Deploy: $initMessage" }
        return liteClient.sendMessage(initMessage)
    }

    suspend fun transfer(
        dest: MsgAddressInt,
        bounce: Boolean,
        coins: Coins,
        seqno: Int,
        comment: String?
    ): LiteServerSendMsgStatus = transfer(dest, bounce, coins, seqno, createCommentPayload(comment))

    suspend fun transfer(
        dest: MsgAddressInt,
        bounce: Boolean,
        coins: Coins,
        seqno: Int,
        payload: Cell,
        destinationStateInit: StateInit? = null,
    ): LiteServerSendMsgStatus {
        val transferMessage =
            createTransferMessage(dest, bounce, coins, seqno, payload, destinationStateInit = destinationStateInit)
        logger.info { "Transfer: $transferMessage" }
        return liteClient.sendMessage(transferMessage)
    }

    override fun createDataInit(): Cell = CellBuilder.createCell {
        storeUInt(0, 32) // seqno
        storeBits(privateKey.publicKey().key.toBitString())
    }

    open fun createSigningMessage(seqno: Int, builder: CellBuilder.() -> Unit = {}): Cell = CellBuilder.createCell {
        storeUInt(seqno, 32)
        apply(builder)
    }

    override fun createExternalInitMessage(): Message<Cell> {
        val stateInit = createStateInit()
        val dest = address(stateInit)
        val signingMessage = createSigningMessage(0)
        val signature = privateKey.sign(signingMessage.hash())
        val body = CellBuilder.createCell {
            storeBytes(signature)
            storeBits(signingMessage.bits)
            storeRefs(signingMessage.refs)
        }
        val info = ExtInMsgInfo(dest)
        return Message(
            info,
            stateInit to null,
            body to null
        )
    }

    fun createTransferMessage(
        dest: MsgAddressInt,
        bounce: Boolean,
        amount: Coins,
        seqno: Int,
        payload: Cell,
        sendMode: Int = 3,
        destinationStateInit: StateInit? = null,
    ): Message<Cell> {
        val stateInit = createStateInit()
        val address = address(stateInit)
        val info = ExtInMsgInfo(address)
        val signingMessage = createSigningMessage(seqno) {
            storeUInt(sendMode, 8)
            storeRef {
                val messageRelaxed = MessageRelaxed(
                    info = CommonMsgInfoRelaxed.IntMsgInfoRelaxed(
                        ihr_disabled = true,
                        bounce = bounce,
                        bounced = false,
                        src = AddrNone,
                        dest = dest,
                        value = CurrencyCollection(
                            coins = amount
                        )
                    ),
                    init = destinationStateInit,
                    body = payload,
                    storeBodyInRef = false
                )
                try {
                    storeTlb(MessageRelaxed.tlbCodec(AnyTlbConstructor), messageRelaxed)
                } catch (e: CellOverflowException) {
                    storeTlb(
                        MessageRelaxed.tlbCodec(AnyTlbConstructor), messageRelaxed.copy(
                            body = Either.of(null, payload)
                        )
                    )
                }
            }
        }
        val signature = privateKey.sign(signingMessage.hash())
        val body = CellBuilder.createCell {
            storeBytes(signature)
            storeBits(signingMessage.bits)
            storeRefs(signingMessage.refs)
        }
        return Message(
            info = info,
            init = null,
            body = body,
            storeInitInRef = false,
            storeBodyInRef = false
        )
    }

    fun createCommentPayload(comment: String? = null): Cell {
        return if (comment == null) {
            Cell.of()
        } else {
            val commentBytes = comment.encodeToByteArray()
            require(commentBytes.size <= 123) { TODO("Commentaries with more than 123 bytes not supported yet. Provided: ${commentBytes.size}") }
            CellBuilder.createCell {
                storeUInt(0, 32)
                storeBytes(commentBytes)
            }
        }
    }

    override fun toString(): String = name
}
