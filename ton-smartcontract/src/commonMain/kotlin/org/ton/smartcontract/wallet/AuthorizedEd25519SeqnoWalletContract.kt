package org.ton.smartcontract.wallet

import org.ton.api.pk.PrivateKeyEd25519
import org.ton.block.*
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.exception.CellOverflowException
import org.ton.cell.storeRef
import org.ton.lite.api.LiteApi
import org.ton.tlb.constructor.AnyTlbConstructor
import org.ton.tlb.storeTlb

/**
 * Base class for almost every wallet contract out there, uses private key for authorization but also requires seqno
 */
abstract class AuthorizedEd25519SeqnoWalletContract(
    private_key: PrivateKeyEd25519,
    override val workchain_id: Int = 0,
) : AuthorizedEd25519WalletContract(private_key, workchain_id), AuthorizedSeqnoWalletContract {
    /**
     * Add seqno and sign message data
     */
    open fun createMessageData(seqno: Int, builder: CellBuilder.() -> Unit = {}) =
        createSignedMessageData {
            storeUInt(seqno, 32)
            apply(builder)
        }

    /**
     * Add latest seqno and sign message data
     */
    suspend fun createMessageData(liteApi: LiteApi, builder: CellBuilder.() -> Unit): Cell =
        createMessageData(seqno(liteApi), builder)

    override fun createExternalInitMessage(): Message<Cell> {
        val stateInit = createStateInit()
        val ownAddress = address()

        return Message(
            info = ExtInMsgInfo(ownAddress),
            init = stateInit,
            body = createMessageData(0)
        )
    }

    override suspend fun createTransferMessage(
        liteApi: LiteApi,
        seqno: Int,
        destination: MsgAddressInt,
        amount: Coins,
        payload: Cell,
        bounce: Boolean,
        sendMode: Int,
        destinationStateInit: StateInit?
    ): Message<Cell> {
        return Message(
            info = ExtInMsgInfo(address()),
            init = null,
            body = createMessageData(seqno) {
                storeUInt(sendMode, 8)
                storeRef {
                    val messageRelaxed = MessageRelaxed(
                        info = CommonMsgInfoRelaxed.IntMsgInfoRelaxed(
                            ihrDisabled = true,
                            bounce = bounce,
                            bounced = false,
                            src = AddrNone,
                            dest = destination,
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
            },
            storeInitInRef = false,
            storeBodyInRef = false
        )
    }

    override fun createDataInit(): Cell = CellBuilder.createCell {
        storeUInt(0, 32) // seqno
        storeBytes(publicKey().key) // public key
    }
}
