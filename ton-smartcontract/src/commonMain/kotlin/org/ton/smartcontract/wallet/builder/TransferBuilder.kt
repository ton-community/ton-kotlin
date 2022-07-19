package org.ton.smartcontract.wallet.builder

import org.ton.block.*
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.exception.CellOverflowException
import org.ton.cell.storeRef
import org.ton.lite.api.LiteApi
import org.ton.tlb.constructor.AnyTlbConstructor
import org.ton.tlb.storeTlb

interface TransferBuilder {
    val lite_api: LiteApi
    val src: MsgAddressInt

    var dest: MsgAddressInt
    var amount: Coins
    var payload: Cell
    var bounce: Boolean
    var send_mode: Int
    var dest_state_init: StateInit?

    var comment: String
        get() = TODO()
        set(value) {
            val commentBytes = comment.encodeToByteArray()
            require(commentBytes.size <= 123) { TODO("comment is too long: ${commentBytes.size} bytes provided, 123 max supported") }
            payload = CellBuilder.createCell {
                storeUInt(0, 32) // op == 0 for comments
                storeBytes(commentBytes)
            }
        }

    fun createData(builder: CellBuilder.() -> Unit = {}): Cell = CellBuilder.createCell { apply(builder) }

    fun createMessage(): Message<Cell> = Message(
        info = ExtInMsgInfo(src),
        init = null,
        body = createData {
            storeUInt(send_mode, 8)
            storeRef {
                val messageRelaxed = MessageRelaxed(
                    info = CommonMsgInfoRelaxed.IntMsgInfoRelaxed(
                        ihrDisabled = true,
                        bounce = bounce,
                        bounced = false,
                        src = AddrNone,
                        dest = dest,
                        value = CurrencyCollection(
                            coins = amount
                        )
                    ),
                    init = dest_state_init,
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
