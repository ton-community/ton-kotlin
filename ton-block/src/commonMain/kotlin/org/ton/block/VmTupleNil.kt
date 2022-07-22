package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor

@SerialName("vm_tuple_nil")
@Serializable
object VmTupleNil : VmTuple {
    override fun depth(): Int = 0

    fun tlbConstructor(): TlbConstructor<VmTupleNil> = VmTupleNilTlbConstructor
}

private object VmTupleNilTlbConstructor : TlbConstructor<VmTupleNil>(
    schema = "vm_tuple_nil\$_ = VmTuple 0;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: VmTupleNil) {
    }

    override fun loadTlb(cellSlice: CellSlice): VmTupleNil = VmTupleNil
}