package org.ton.block.tlb

import org.ton.block.VmStackList
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

object VmStackListCombinator : TlbCombinator<VmStackList>(
    VmStackListConsConstructor, VmStackListNilConstructor
) {
    override fun getConstructor(value: VmStackList): TlbConstructor<out VmStackList> = when (value) {
        is VmStackList.Cons -> VmStackListConsConstructor
        is VmStackList.Nil -> VmStackListNilConstructor
    }
}

object VmStackListConsConstructor : TlbConstructor<VmStackList.Cons>(
    schema = "vm_stk_cons#_ {n:#} rest:^(VmStackList n) tos:VmStackValue = VmStackList (n + 1);"
) {
    override fun encode(
        cellBuilder: CellBuilder,
        value: VmStackList.Cons,
        param: Int,
        negativeParam: (Int) -> Unit
    ) = cellBuilder {
        val n = param - 1
        storeRef {
            storeTlb(value.rest, VmStackListCombinator, n)
        }
        storeTlb(value.tos, VmStackValueTlbCombinator)
    }

    override fun decode(
        cellSlice: CellSlice,
        param: Int,
        negativeParam: (Int) -> Unit
    ): VmStackList.Cons = cellSlice {
        val n = param - 1
        val rest = loadRef {
            loadTlb(VmStackListCombinator, n)
        }
        val tos = loadTlb(VmStackValueTlbCombinator, n)
        VmStackList.Cons(rest, tos)
    }
}

object VmStackListNilConstructor : TlbConstructor<VmStackList.Nil>(
    schema = "vm_stk_nil#_ = VmStackList 0;"
) {
    override fun encode(
        cellBuilder: CellBuilder,
        value: VmStackList.Nil,
        param: Int,
        negativeParam: (Int) -> Unit
    ) {
    }

    override fun decode(
        cellSlice: CellSlice,
        param: Int,
        negativeParam: (Int) -> Unit
    ): VmStackList.Nil {
        return VmStackList.Nil
    }
}
