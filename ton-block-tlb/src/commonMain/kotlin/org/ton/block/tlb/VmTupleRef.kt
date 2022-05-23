package org.ton.block.tlb

import org.ton.block.VmTupleRef
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

object VmTupleRefTlbCombinator : TlbCombinator<VmTupleRef>(
    VmTupleRefNilTlbConstructor,
    VmTupleRefSingleTlbConstructor,
    VmTupleRefAnyTlbConstructor
) {
    override fun getConstructor(value: VmTupleRef): TlbConstructor<out VmTupleRef> = when (value) {
        is VmTupleRef.Nil -> VmTupleRefNilTlbConstructor
        is VmTupleRef.Single -> VmTupleRefSingleTlbConstructor
        is VmTupleRef.Any -> VmTupleRefAnyTlbConstructor
    }
}

object VmTupleRefNilTlbConstructor : TlbConstructor<VmTupleRef.Nil>(
    schema = "vm_tupref_nil$_ = VmTupleRef 0;"
) {
    override fun encode(
        cellBuilder: CellBuilder, value: VmTupleRef.Nil, param: Int, negativeParam: (Int) -> Unit
    ) {
    }

    override fun decode(cellSlice: CellSlice, param: Int, negativeParam: (Int) -> Unit): VmTupleRef.Nil {
        return VmTupleRef.Nil
    }
}

object VmTupleRefSingleTlbConstructor : TlbConstructor<VmTupleRef.Single>(
    schema = "vm_tupref_single$_ entry:^VmStackValue = VmTupleRef 1;"
) {
    override fun encode(
        cellBuilder: CellBuilder, value: VmTupleRef.Single, param: Int, negativeParam: (Int) -> Unit
    ) = cellBuilder {
        storeRef {
            storeTlb(value.entry, VmStackValueTlbCombinator)
        }
    }

    override fun decode(
        cellSlice: CellSlice, param: Int, negativeParam: (Int) -> Unit
    ): VmTupleRef.Single = cellSlice {
        val entry = loadRef {
            loadTlb(VmStackValueTlbCombinator)
        }
        VmTupleRef.Single(entry)
    }
}

object VmTupleRefAnyTlbConstructor : TlbConstructor<VmTupleRef.Any>(
    schema = "vm_tupref_any$_ {n:#} ref:^(VmTuple (n + 2)) = VmTupleRef (n + 2);"
) {
    override fun encode(
        cellBuilder: CellBuilder, value: VmTupleRef.Any, param: Int, negativeParam: (Int) -> Unit
    ) = cellBuilder {
        val n = param - 2
        storeRef {
            storeTlb(value.ref, VmTupleCombinator, n)
        }
    }

    override fun decode(
        cellSlice: CellSlice, param: Int, negativeParam: (Int) -> Unit
    ): VmTupleRef.Any = cellSlice {
        val n = param - 2
        val ref = loadRef {
            loadTlb(VmTupleCombinator, n)
        }
        VmTupleRef.Any(ref)
    }
}
