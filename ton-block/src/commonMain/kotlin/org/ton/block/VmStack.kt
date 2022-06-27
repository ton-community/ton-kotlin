package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.*
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@SerialName("vm_stack")
@Serializable
data class VmStack(
    val depth: Int,
    val stack: VmStackList
) {
    constructor(stack: VmStackList) : this(stack.count(), stack)

    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<VmStack> = VmStackTlbConstructor
    }
}

private object VmStackTlbConstructor : TlbConstructor<VmStack>(
    schema = "vm_stack#_ depth:(## 24) stack:(VmStackList depth) = VmStack;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: VmStack
    ) = cellBuilder {
        storeUInt(value.depth, 24)
        storeTlb(VmStackList.tlbCodec(value.depth), value.stack)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): VmStack = cellSlice {
        val depth = loadUInt(24).toInt()
        val stack = loadTlb(VmStackList.tlbCodec(depth))
        VmStack(depth, stack)
    }
}
