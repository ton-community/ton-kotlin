package org.ton.block.tlb

import org.ton.block.VmStack
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

object VmStackTlbConstructor : TlbConstructor<VmStack>(
    schema = "vm_stack#_ depth:(## 24) stack:(VmStackList depth) = VmStack;"
) {
    override fun encode(
        cellBuilder: CellBuilder,
        value: VmStack,
        param: Int,
        negativeParam: (Int) -> Unit
    ) = cellBuilder {
        storeUInt(value.depth, 24)
        storeTlb(value.stack, VmStackListCombinator, value.depth)
    }

    override fun decode(
        cellSlice: CellSlice,
        param: Int,
        negativeParam: (Int) -> Unit
    ): VmStack = cellSlice {
        val depth = loadUInt(24).toInt()
        val stack = loadTlb(VmStackListCombinator, depth)
        VmStack(depth, stack)
    }
}
