package org.ton.block.tlb

import org.ton.block.*
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.constructor.tlbCodec
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

fun <X : Any> Message.Companion.tlbCodec(x: TlbCodec<X>): TlbCodec<Message<X>> = MessageTlbConstructor(x)

private class MessageTlbConstructor<X : Any>(
    x: TlbCodec<X>
) : TlbConstructor<Message<X>>(
    schema = "message\$_ {X:Type} info:CommonMsgInfo " +
            "init:(Maybe (Either StateInit ^StateInit)) " +
            "body:(Either X ^X) = Message X;"
) {
    private val commonMsgInfoCodec = CommonMsgInfo.tlbCodec()
    private val stateInitCodec = StateInit.tlbCodec()
    private val referencedStateInitCodec = Cell.tlbCodec(stateInitCodec)
    private val eitherStateInitCodec = Either.tlbCodec(stateInitCodec, referencedStateInitCodec)
    private val maybeEitherCodec = Maybe.tlbCodec(eitherStateInitCodec)
    private val referencedXCodec = Cell.tlbCodec(x)
    private val eitherXCodec = Either.tlbCodec(x, referencedXCodec)

    override fun encode(
        cellBuilder: CellBuilder, value: Message<X>, param: Int, negativeParam: (Int) -> Unit
    ) = cellBuilder {
        storeTlb(value.info, commonMsgInfoCodec)
        storeTlb(value.init, maybeEitherCodec)
        storeTlb(value.body, eitherXCodec)
    }

    override fun decode(
        cellSlice: CellSlice, param: Int, negativeParam: (Int) -> Unit
    ): Message<X> = cellSlice {
        val info = loadTlb(commonMsgInfoCodec)
        val init = loadTlb(maybeEitherCodec)
        val body = loadTlb(eitherXCodec)
        Message(info, init, body)
    }
}
