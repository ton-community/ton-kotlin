package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbConstructor

@Serializable
@SerialName("vm_stk_builder")
class VmStackBuilder(
    val cell: Cell
) : VmStackValue {
    constructor(cellBuilder: CellBuilder) : this(cellBuilder.endCell())

    fun toCellBuilder(): CellBuilder = CellBuilder(cell)

    companion object {
        fun tlbConstructor(): TlbConstructor<VmStackBuilder> = VmStackValueBuilderTlbConstructor
    }
}

private object VmStackValueBuilderTlbConstructor : TlbConstructor<VmStackBuilder>(
    schema = "vm_stk_builder#05 cell:^Cell = VmStackValue;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: VmStackBuilder
    ) = cellBuilder {
        storeRef(value.cell)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): VmStackBuilder = cellSlice {
        val cell = loadRef()
        VmStackBuilder(cell)
    }
}