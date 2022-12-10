@file:Suppress("OPT_IN_USAGE", "NOTHING_TO_INLINE")

package org.ton.hashmap

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.*
import kotlin.jvm.JvmStatic

inline fun Unary(depth: Int): Unary = Unary.of(depth)

@Serializable
@JsonClassDiscriminator("@type")
sealed class Unary {
    companion object : TlbNegatedCodec<Unary> by UnaryTlbCombinator {
        @JvmStatic
        fun of(depth: Int): Unary {
            var unary: Unary = UnaryZero
            repeat(depth) {
                unary = UnarySuccess(unary)
            }
            return unary
        }

        @JvmStatic
        fun tlbCodec(): TlbNegatedCodec<Unary> = UnaryTlbCombinator
    }
}

private object UnaryTlbCombinator : TlbNegatedCombinator<Unary>(
    Unary::class,
    UnaryZero::class to UnaryZeroTlbConstructor,
    UnarySuccess::class to UnarySuccessTlbConstructor,
)

private object UnarySuccessTlbConstructor : TlbNegatedConstructor<UnarySuccess>(
    schema = "unary_succ\$1 {n:#} x:(Unary ~n) = Unary ~(n + 1);"
) {
    override fun storeNegatedTlb(cellBuilder: CellBuilder, value: UnarySuccess): Int {
        return cellBuilder.storeNegatedTlb(Unary, value.x) + 1
    }

    override fun loadNegatedTlb(cellSlice: CellSlice): Pair<Int, UnarySuccess> {
        val (n, x) = cellSlice.loadNegatedTlb(Unary)
        return n + 1 to UnarySuccess(x)
    }
}

private object UnaryZeroTlbConstructor : TlbNegatedConstructor<UnaryZero>(
    schema = "unary_zero\$0 = Unary ~0;"
) {
    override fun storeNegatedTlb(cellBuilder: CellBuilder, value: UnaryZero): Int = 0

    override fun loadNegatedTlb(cellSlice: CellSlice): Pair<Int, UnaryZero> = 0 to UnaryZero
}
