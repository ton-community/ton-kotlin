package org.ton.block.tlb

import org.ton.block.Message
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor

// TODO
//fun <X:Any> Message.Companion.tlbCodec(x: TlbCodec<X>): TlbCodec<Message<X>> = MessageTlbConstructor(x)

private class MessageTlbConstructor<X : Any>(
    val x: TlbCodec<X>
) : TlbConstructor<Message<X>>(
    schema = "message\$_ {X:Type} info:CommonMsgInfo " +
            "init:(Maybe (Either StateInit ^StateInit)) " +
            "body:(Either X ^X) = Message X;"
) {
//    private val commonMsgInfoCodec = CommonMsgInfo.tlbCodec()
//    private val stateInitCodec = StateInit.tlbCodec()
//    private val referencedStateInitCodec = Cell.tlbCodec(stateInitCodec)
//    private val eitherStateInitCodec = Either.tlbCodec(stateInitCodec, referencedStateInitCodec)
//    private val maybeEitherCodec = Maybe.tlbCodec(eitherStateInitCodec)
//    private val referencedXCodec = Cell.tlbCodec(x)
//    private val eitherXCodec = Either.tlbCodec(x, referencedXCodec)

    override fun encode(
        cellBuilder: CellBuilder, value: Message<X>, param: Int, negativeParam: (Int) -> Unit
    ) = cellBuilder {
        TODO("Not yet implemented")
    }

    override fun decode(cellSlice: CellSlice, param: Int, negativeParam: (Int) -> Unit): Message<X> {
        TODO("Not yet implemented")
    }
}
