package org.ton.block.tlb

import org.ton.block.Just
import org.ton.block.Maybe
import org.ton.block.Nothing
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.*

class MaybeTlbCombinator<T : Any>(
    typeCodec: TlbCodec<T>
) : TlbCombinator<Maybe<T>>() {
    private val nothingConstructor = NothingConstructor<T>()
    private val justConstructor = JustConstructor(typeCodec)

    override val constructors: List<TlbConstructor<out Maybe<T>>> = listOf(
        nothingConstructor, justConstructor
    )

    override fun getConstructor(value: Maybe<T>): TlbConstructor<out Maybe<T>> = when (value) {
        is Just -> justConstructor
        is Nothing -> nothingConstructor
    }
}

class NothingConstructor<T : Any> : TlbConstructor<Nothing<T>>(
    schema = "nothing\$0 {X:Type} = Maybe X;"
) {
    private val nothing = Nothing<T>()

    override fun encode(
        cellBuilder: CellBuilder,
        value: Nothing<T>,
        param: Int,
        negativeParam: (Int) -> Unit
    ) {
    }

    override fun decode(
        cellSlice: CellSlice,
        param: Int,
        negativeParam: (Int) -> Unit
    ): Nothing<T> = nothing
}

class JustConstructor<X : Any>(
    val typeCodec: TlbCodec<X>
) : TlbConstructor<Just<X>>(
    schema = "just\$1 {X:Type} value:X = Maybe X;"
) {
    override fun encode(
        cellBuilder: CellBuilder,
        value: Just<X>,
        param: Int,
        negativeParam: (Int) -> Unit
    ) = cellBuilder {
        storeTlb(value.value, typeCodec, param, negativeParam)
    }

    override fun decode(
        cellSlice: CellSlice,
        param: Int,
        negativeParam: (Int) -> Unit
    ): Just<X> = cellSlice {
        val value = cellSlice.loadTlb(typeCodec, param, negativeParam)
        Just(value)
    }
}
