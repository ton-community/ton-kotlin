package org.ton.hashmap.tlb

import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.hashmap.Unary
import org.ton.hashmap.UnarySuccess
import org.ton.hashmap.UnaryZero
import org.ton.tlb.*

fun Unary.Companion.tlbCodec(): TlbNegatedCodec<Unary> = UnaryTlbCombinator()

private class UnaryTlbCombinator : TlbNegatedCombinator<Unary>() {
    private val unarySuccessConstructor by lazy {
        UnarySuccessTlbConstructor()
    }
    private val unaryZeroConstructor by lazy {
        UnaryZeroTlbConstructor()
    }

    override val constructors: List<TlbNegatedConstructor<out Unary>> by lazy {
        listOf(unarySuccessConstructor, unaryZeroConstructor)
    }

    override fun getConstructor(value: Unary): TlbNegatedConstructor<out Unary> = when (value) {
        is UnarySuccess -> unarySuccessConstructor
        is UnaryZero -> unaryZeroConstructor
    }

    private class UnarySuccessTlbConstructor : TlbNegatedConstructor<UnarySuccess>(
        schema = "unary_succ\$1 {n:#} x:(Unary ~n) = Unary ~(n + 1);"
    ), TlbNegatedCodec<UnarySuccess> {
        private val unaryCodec by lazy { Unary.tlbCodec() }

        override fun storeNegatedTlb(cellBuilder: CellBuilder, value: UnarySuccess): Int {
            return cellBuilder.storeTlb(unaryCodec, value) + 1
        }

        override fun loadNegatedTlb(cellSlice: CellSlice): Pair<Int, UnarySuccess> {
            val (n, x) = cellSlice.loadTlb(unaryCodec)
            return n + 1 to UnarySuccess(x)
        }
    }

    private class UnaryZeroTlbConstructor : TlbNegatedConstructor<UnaryZero>(
        schema = "unary_zero\$0 = Unary ~0;"
    ) {
        override fun storeNegatedTlb(cellBuilder: CellBuilder, value: UnaryZero): Int = 0

        override fun loadNegatedTlb(cellSlice: CellSlice): Pair<Int, UnaryZero> = 0 to UnaryZero
    }
}
