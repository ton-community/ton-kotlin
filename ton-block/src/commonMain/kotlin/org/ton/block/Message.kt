package org.ton.block

import kotlinx.serialization.Serializable
import org.ton.cell.Cell
import org.ton.cell.*
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
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
        @JvmStatic
        fun <X> tlbCodec(
            x: TlbCodec<X>
        ): TlbConstructor<Message<X>> = MessageTlbConstructor(x)
    }

    override fun toString(): String {
        return "Message(info=$info, init=$init, body=$body)".replace("\n", "")
    }
}

private class MessageTlbConstructor<X>(
    x: TlbCodec<X>
) : TlbConstructor<Message<X>>(
    schema = "message\$_ {X:Type} info:CommonMsgInfo " +
            "init:(Maybe (Either StateInit ^StateInit)) " +
            "body:(Either X ^X) = Message X;"
) {
    private val commonMsgInfoCodec by lazy { CommonMsgInfo.tlbCodec() }
    private val stateInitCodec by lazy { StateInit.tlbCodec() }
    private val referencedStateInitCodec by lazy { Cell.tlbCodec(stateInitCodec) }
    private val eitherStateInitCodec by lazy { Either.tlbCodec(stateInitCodec, referencedStateInitCodec) }
    private val maybeEitherCodec by lazy { Maybe.tlbCodec(eitherStateInitCodec) }
    private val referencedXCodec by lazy { Cell.tlbCodec(x) }
    private val eitherXCodec by lazy { Either.tlbCodec(x, referencedXCodec) }

    override fun storeTlb(
        cellBuilder: CellBuilder, value: Message<X>
    ) = cellBuilder {
        storeTlb(commonMsgInfoCodec, value.info)
        storeTlb(maybeEitherCodec, value.init)
        storeTlb(eitherXCodec, value.body)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): Message<X> = cellSlice {
        val info = loadTlb(commonMsgInfoCodec)
        val init = loadTlb(maybeEitherCodec)
        val body = loadTlb(eitherXCodec)
        Message(info, init, body)
    }
}

