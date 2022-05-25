package org.ton.block.tlb

import org.ton.block.Just
import org.ton.block.Maybe
import org.ton.block.Nothing
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.*

fun <X : Any> Maybe.Companion.tlbCodec(typeCodec: TlbCodec<X>): TlbCodec<Maybe<X>> = MaybeTlbCombinator(typeCodec)

private class MaybeTlbCombinator<X : Any>(
    typeCodec: TlbCodec<X>
) : TlbCombinator<Maybe<X>>() {
    private val nothingConstructor by lazy {
        NothingConstructor<X>()
    }
    private val justConstructor by lazy {
        JustConstructor(typeCodec)
    }

    override val constructors: List<TlbConstructor<out Maybe<X>>> by lazy {
        listOf(
            nothingConstructor, justConstructor
        )
    }

    override fun getConstructor(value: Maybe<X>): TlbConstructor<out Maybe<X>> = when (value) {
        is Just -> justConstructor
        is Nothing -> nothingConstructor
    }

    private class NothingConstructor<X : Any> : TlbConstructor<Nothing<X>>(
        schema = "nothing\$0 {X:Type} = Maybe X;"
    ) {
        private val nothing = Nothing<X>()

        override fun storeTlb(
            cellBuilder: CellBuilder,
            value: Nothing<X>
        ) {
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): Nothing<X> = nothing
    }

    private class JustConstructor<X : Any>(
        val typeCodec: TlbCodec<X>
    ) : TlbConstructor<Just<X>>(
        schema = "just\$1 {X:Type} value:X = Maybe X;"
    ) {
        override fun storeTlb(
            cellBuilder: CellBuilder,
            value: Just<X>
        ) = cellBuilder {
            storeTlb(typeCodec, value.value)
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): Just<X> = cellSlice {
            val value = cellSlice.loadTlb(typeCodec)
            Just(value)
        }
    }
}
