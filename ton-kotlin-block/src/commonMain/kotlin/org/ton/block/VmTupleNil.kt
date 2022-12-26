package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

@SerialName("vm_tuple_nil")
@Serializable
public object VmTupleNil : VmTuple, Sequence<VmStackValue> by emptySequence(),
    TlbConstructorProvider<VmTupleNil> by VmTupleNilTlbConstructor {
    override fun depth(): Int = 0

    override fun toString(): String = "vm_tuple_nil"
}

private object VmTupleNilTlbConstructor : TlbConstructor<VmTupleNil>(
    schema = "vm_tuple_nil\$_ = VmTuple 0;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: VmTupleNil) {
    }

    override fun loadTlb(cellSlice: CellSlice): VmTupleNil = VmTupleNil
}
