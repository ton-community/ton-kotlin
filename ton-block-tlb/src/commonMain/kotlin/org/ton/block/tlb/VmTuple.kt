package org.ton.block.tlb

import org.ton.block.VmTuple
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

object VmTupleCombinator : TlbCombinator<VmTuple>(
    Nil, TCons
) {
    override fun getConstructor(value: VmTuple): TlbConstructor<out VmTuple> = when (value) {
        is VmTuple.Nil -> Nil
        is VmTuple.TCons -> TCons
    }

    object Nil : TlbConstructor<VmTuple.Nil>(
        schema = "vm_tuple_nil$_ = VmTuple 0;"
    ) {
        override fun encode(cellBuilder: CellBuilder, value: VmTuple.Nil, param: Int, negativeParam: (Int) -> Unit) {
        }

        override fun decode(cellSlice: CellSlice, param: Int, negativeParam: (Int) -> Unit): VmTuple.Nil {
            return VmTuple.Nil
        }
    }

    object TCons : TlbConstructor<VmTuple.TCons>(
        schema = "vm_tuple_tcons$_ {n:#} head:(VmTupleRef n) tail:^VmStackValue = VmTuple (n + 1);"
    ) {
        override fun encode(
            cellBuilder: CellBuilder, value: VmTuple.TCons, param: Int, negativeParam: (Int) -> Unit
        ) = cellBuilder {
            val n = param - 1
            storeTlb(value.head, VmTupleRefTlbCombinator, n)
            storeRef {
                storeTlb(value.tail, VmStackValueTlbCombinator)
            }
        }

        override fun decode(
            cellSlice: CellSlice, param: Int, negativeParam: (Int) -> Unit
        ): VmTuple.TCons = cellSlice {
            val n = param - 1
            val head = loadTlb(VmTupleRefTlbCombinator, n)
            val tail = loadRef {
                loadTlb(VmStackValueTlbCombinator)
            }
            VmTuple.TCons(head, tail)
        }
    }
}

