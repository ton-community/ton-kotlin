package org.ton.smartcontract.wallet

import org.ton.api.pk.PrivateKeyEd25519
import org.ton.api.pub.PublicKeyEd25519
import org.ton.block.*
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.exception.CellOverflowException
import org.ton.lite.api.LiteApi
import org.ton.lite.api.liteserver.LiteServerSendMsgStatus
import org.ton.logger.Logger
import org.ton.smartcontract.SmartContract
import org.ton.tlb.constructor.AnyTlbConstructor
import org.ton.tlb.storeTlb

abstract class WalletContract(
    override val liteApi: LiteApi,
    val privateKey: PrivateKeyEd25519,
    override val workchainId: Int = 0
) : SmartContract {
    val logger: Logger by lazy {
        Logger.println(name, Logger.Level.DEBUG)
    }

    val publicKey: PublicKeyEd25519 by lazy {
        privateKey.publicKey()
    }

    override suspend fun deploy(): LiteServerSendMsgStatus {
        val initMessage = createExternalInitMessage()
        logger.info { "Deploy: $initMessage" }
        return liteApi.sendMessage(initMessage)
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
        payload: Cell
    ): LiteServerSendMsgStatus {
        val transferMessage = createTransferMessage(dest, bounce, coins, seqno, payload)
        logger.info { "Transfer: $transferMessage" }
        return liteApi.sendMessage(transferMessage)
    }

    override fun createDataInit(): Cell = CellBuilder.createCell {
        storeUInt(0, 32) // seqno
        storeBytes(publicKey.key)
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
        val info = CommonMsgInfo.ExtInMsgInfo(dest)
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
        sendMode: Int = 3
    ): Message<Cell> {
        val stateInit = createStateInit()
        val address = address(stateInit)
        val info = CommonMsgInfo.ExtInMsgInfo(address)
        val signingMessage = createSigningMessage(seqno) {
            storeUInt(sendMode, 8)
            storeRef {
                val messageRelaxed = MessageRelaxed(
                    info = CommonMsgInfoRelaxed.IntMsgInfo(
                        ihrDisabled = true,
                        bounce = bounce,
                        bounced = false,
                        src = MsgAddressExtNone,
                        dest = dest,
                        value = CurrencyCollection(
                            coins = amount
                        )
                    ),
                    init = null,
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
