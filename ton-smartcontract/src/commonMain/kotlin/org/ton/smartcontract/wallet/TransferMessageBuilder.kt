package org.ton.smartcontract.wallet

import org.ton.block.*
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.exception.CellOverflowException
import org.ton.cell.storeRef
import org.ton.lite.api.LiteApi
import org.ton.tlb.constructor.AnyTlbConstructor
import org.ton.tlb.storeTlb

interface TransferMessageBuilder {
    val liteApi: LiteApi
    val source: MsgAddressInt
    var destination: MsgAddressInt
    var amount: Coins
    var payload: Cell
    var bounce: Boolean
    var sendMode: Int
    var destinationStateInit: StateInit?

    fun buildData(builder: CellBuilder.() -> Unit = {}): Cell = CellBuilder.createCell { apply(builder) }

    fun build(): Message<Cell> = Message(
        info = ExtInMsgInfo(source),
        init = null,
        body = buildData {
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
