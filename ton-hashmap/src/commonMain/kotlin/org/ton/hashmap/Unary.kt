@file:Suppress("OPT_IN_USAGE", "NOTHING_TO_INLINE")

package org.ton.hashmap

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.cell.*
import org.ton.tlb.*

inline fun Unary(depth: Int): Unary = Unary.of(depth)

@Serializable
@JsonClassDiscriminator("@type")
sealed class Unary {
    companion object {
        @JvmStatic
        fun of(depth: Int): Unary {
            var unary: Unary = UnaryZero
            repeat(depth) {
                unary = UnarySuccess(unary)
            }
            return unary
        }

        @JvmStatic
        fun tlbCodec(): TlbNegatedCodec<Unary> = UnaryTlbCombinator()
    }
}

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
    ) {
        private val unaryCodec by lazy { Unary.tlbCodec() }

        override fun storeNegatedTlb(cellBuilder: CellBuilder, value: UnarySuccess): Int {
            return cellBuilder.storeNegatedTlb(unaryCodec, value.x) + 1
        }

        override fun loadNegatedTlb(cellSlice: CellSlice): Pair<Int, UnarySuccess> {
            val (n, x) = cellSlice.loadNegatedTlb(unaryCodec)
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

