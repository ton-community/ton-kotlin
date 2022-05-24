package org.ton.hashmap.tlb

import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.hashmap.Unary
import org.ton.hashmap.UnarySuccess
import org.ton.hashmap.UnaryZero
import org.ton.tlb.*

fun Unary.Companion.tlbCodec(): TlbCodec<Unary> = UnaryTlbCombinator()

private class UnaryTlbCombinator : TlbCombinator<Unary>(
    UnaryZeroTlbConstructor,
    UnarySuccessTlbConstructor
) {
    override fun getConstructor(value: Unary): TlbConstructor<out Unary> = when (value) {
        is UnarySuccess -> UnarySuccessTlbConstructor
        is UnaryZero -> UnaryZeroTlbConstructor
    }

    object UnarySuccessTlbConstructor : TlbConstructor<UnarySuccess>(
        schema = "unary_succ\$1 {n:#} x:(Unary ~n) = Unary ~(n + 1);",
        id = BitString.binary("1")
    ) {
        private val unaryCodec = Unary.tlbCodec()

        override fun encode(
            cellBuilder: CellBuilder,
            value: UnarySuccess,
            param: Int,
            negativeParam: (Int) -> Unit
        ) = cellBuilder {
            var n = 0
            storeTlb(value.x, unaryCodec) { n = it }
            negativeParam(n + 1)
        }

        override fun decode(
            cellSlice: CellSlice,
            param: Int,
            negativeParam: (Int) -> Unit,
        ): UnarySuccess = cellSlice {
            var n = 0
            val x = loadTlb(unaryCodec) { n = it }
            negativeParam(n + 1)
            UnarySuccess(x)
        }
    }

    object UnaryZeroTlbConstructor : TlbConstructor<UnaryZero>(
        schema = "unary_zero\$0 = Unary ~0;",
        id = BitString.binary("0")
    ) {
        override fun encode(
            cellBuilder: CellBuilder,
            value: UnaryZero,
            param: Int,
            negativeParam: (Int) -> Unit
        ) = cellBuilder {
            negativeParam(0)
        }

        override fun decode(
            cellSlice: CellSlice,
            param: Int,
            negativeParam: (Int) -> Unit
        ): UnaryZero = cellSlice {
            negativeParam(0)
            UnaryZero
        }
    }
}
