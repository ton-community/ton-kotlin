package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor

@SerialName("vm_stk_null")
@Serializable
object VmStackNull : VmStackValue {
    fun tlbConstructor(): TlbConstructor<VmStackNull> = VmStackValueNullConstructor
}

private object VmStackValueNullConstructor : TlbConstructor<VmStackNull>(
    schema = "vm_stk_null#00 = VmStackValue;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: VmStackNull
    ) {
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): VmStackNull = VmStackNull
}
