package org.ton.block

import kotlinx.serialization.Serializable
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.constructor.tlbCodec
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@Serializable
data class MessageRelaxed<X : Any>(
    val info: CommonMsgInfoRelaxed,
    val init: Maybe<Either<StateInit, StateInit>>,
    val body: Either<X, X>
) {
    constructor(
        info: CommonMsgInfoRelaxed,
        init: StateInit? = null,
        body: X? = null,
        storeInitInRef: Boolean = true,
        storeBodyInRef: Boolean = true
    ) : this(
        info = info,
        init = Maybe.of(
            if (init != null) {
                if (storeInitInRef) Either.of(null, init)
                else Either.of(init, null)
            } else null
        ),
        body = if (storeBodyInRef) Either.of(null, body)
        else Either.of(body, null)
    )

    companion object {
        @JvmStatic
        fun <X : Any> tlbCodec(x: TlbCodec<X>): TlbCodec<MessageRelaxed<X>> = MessageRelaxedTlbConstructor(x)
    }
}

private class MessageRelaxedTlbConstructor<X : Any>(
    x: TlbCodec<X>
) : TlbConstructor<MessageRelaxed<X>>(
    schema = "message\$_ {X:Type} info:CommonMsgInfoRelaxed " +
            "init:(Maybe (Either StateInit ^StateInit)) " +
            "body:(Either X ^X) = MessageRelaxed X;"
) {
    private val commonMsgInfoCodec by lazy { CommonMsgInfoRelaxed.tlbCodec() }
    private val stateInitCodec by lazy { StateInit.tlbCodec() }
    private val referencedStateInitCodec by lazy { Cell.tlbCodec(stateInitCodec) }
    private val eitherStateInitCodec by lazy { Either.tlbCodec(stateInitCodec, referencedStateInitCodec) }
    private val maybeEitherCodec by lazy { Maybe.tlbCodec(eitherStateInitCodec) }
    private val referencedXCodec by lazy { Cell.tlbCodec(x) }
    private val eitherXCodec by lazy { Either.tlbCodec(x, referencedXCodec) }

    override fun storeTlb(
        cellBuilder: CellBuilder, value: MessageRelaxed<X>
    ) = cellBuilder {
        storeTlb(commonMsgInfoCodec, value.info)
        storeTlb(maybeEitherCodec, value.init)
        storeTlb(eitherXCodec, value.body)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): MessageRelaxed<X> = cellSlice {
        val info = loadTlb(commonMsgInfoCodec)
        val init = loadTlb(maybeEitherCodec)
        val body = loadTlb(eitherXCodec)
        MessageRelaxed(info, init, body)
    }
}
