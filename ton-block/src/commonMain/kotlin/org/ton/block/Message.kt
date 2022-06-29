package org.ton.block

import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.constructor.AnyTlbConstructor
import org.ton.tlb.constructor.tlbCodec
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@Serializable
data class Message<X>(
    val info: CommonMsgInfo,
    val init: Maybe<Either<StateInit, StateInit>>,
    val body: Either<X, X>
) {
    constructor(
        info: CommonMsgInfo,
        init: Pair<StateInit?, StateInit?>?,
        body: Pair<X?, X?>
    ) : this(info, init?.toEither().toMaybe(), body.toEither())

    constructor(
        info: CommonMsgInfo,
        init: StateInit?,
        body: X?,
        storeInitInRef: Boolean = true,
        storeBodyInRef: Boolean = true
    ) : this(
        info = info,
        init = init?.let {
            if (storeInitInRef) null to init else init to null
        },
        body = if (storeBodyInRef) null to body else body to null
    )

    companion object {
        val Any = tlbCodec(AnyTlbConstructor)

        @JvmStatic
        fun <X> tlbCodec(
            x: TlbCodec<X>
        ): TlbConstructor<Message<X>> = MessageTlbConstructor(x)
    }

    override fun toString(): String {
        return "Message(info=$info, init=$init, body=$body)".replace("\n", "")
    }
}

operator fun <X> Message.Companion.invoke(x: TlbCodec<X>) = tlbCodec(x)

private class MessageTlbConstructor<X>(
    x: TlbCodec<X>
) : TlbConstructor<Message<X>>(
    schema = "message\$_ {X:Type} info:CommonMsgInfo " +
            "init:(Maybe (Either StateInit ^StateInit)) " +
            "body:(Either X ^X) = Message X;",
    id = BitString.empty()
) {
    private val Body = Either(x, Cell.tlbCodec(x))

    override fun storeTlb(
        cellBuilder: CellBuilder, value: Message<X>
    ) = cellBuilder {
        storeTlb(CommonMsgInfo, value.info)
        storeTlb(Init, value.init)
        storeTlb(Body, value.body)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): Message<X> = cellSlice {
        val info = loadTlb(CommonMsgInfo)
        val init = loadTlb(Init)
        val body = loadTlb(Body)
        Message(info, init, body)
    }

    companion object {
        private val Init = Maybe(Either(StateInit, Cell.tlbCodec(StateInit)))
    }
}

