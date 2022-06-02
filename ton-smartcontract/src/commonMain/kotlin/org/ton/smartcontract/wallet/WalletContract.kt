package org.ton.smartcontract.wallet

import org.ton.api.pk.PrivateKeyEd25519
import org.ton.api.pub.PublicKeyEd25519
import org.ton.block.*
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.lite.api.LiteApi
import org.ton.lite.api.liteserver.LiteServerSendMsgStatus
import org.ton.smartcontract.SmartContract
import org.ton.tlb.constructor.AnyTlbConstructor
import org.ton.tlb.storeTlb

abstract class WalletContract(
    val privateKey: PrivateKeyEd25519,
    override val workchainId: Int = 0
) : SmartContract {
    val publicKey: PublicKeyEd25519 by lazy {
        privateKey.publicKey()
    }

    override suspend fun deploy(liteApi: LiteApi): LiteServerSendMsgStatus {
        val initMessage = createInitMessage()
        return liteApi.sendMessage(initMessage)
    }

    suspend fun transfer(
        liteApi: LiteApi,
        dest: MsgAddressInt,
        seqno: Int,
        coins: Coins,
        comment: String?,
        bounce: Boolean
    ): LiteServerSendMsgStatus = transfer(liteApi, dest, seqno, coins, createCommentPayload(comment), bounce)

    suspend fun transfer(
        liteApi: LiteApi,
        dest: MsgAddressInt,
        seqno: Int,
        coins: Coins,
        payload: Cell,
        bounce: Boolean
    ): LiteServerSendMsgStatus {
        val transferMessage = createTransferMessage(dest, seqno, coins, payload, bounce)
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

    fun createInitMessage(): Message<Cell> {
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
        seqno: Int,
        amount: Coins,
        payload: Cell,
        bounce: Boolean,
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
                        src = MsgAddressExt.AddrNone,
                        dest = dest,
                        value = CurrencyCollection(
                            coins = amount
                        )
                    ),
                    init = null,
                    body = payload
                )
                storeTlb(MessageRelaxed.tlbCodec(AnyTlbConstructor), messageRelaxed)
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
            body = body
        )
    }

    fun createCommentPayload(comment: String? = null): Cell {
        return if (comment == null) {
            Cell.of()
        } else {
            CellBuilder.createCell {
                storeUInt(0, 32)
                storeBytes(comment.encodeToByteArray())
            }
        }
    }

    override fun toString(): String = name
}
