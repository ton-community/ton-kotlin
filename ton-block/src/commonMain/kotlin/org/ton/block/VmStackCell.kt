package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbConstructor

@Serializable
@SerialName("vm_stk_cell")
data class VmStackCell(
    val cell: org.ton.cell.Cell
) : VmStackValue {
    companion object {
        fun tlbConstructor(): TlbConstructor<VmStackCell> = VmStackValueCellConstructor
    }
}

private object VmStackValueCellConstructor : TlbConstructor<VmStackCell>(
    schema = "vm_stk_cell#03 cell:^Cell = VmStackValue;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: VmStackCell
    ) = cellBuilder {
        storeRef(value.cell)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): VmStackCell = cellSlice {
        val cell = loadRef()
        VmStackCell(cell)
    }
}