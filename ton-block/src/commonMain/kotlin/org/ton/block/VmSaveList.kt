package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.hashmap.HashMapE
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@SerialName("_")
@Serializable
data class VmSaveList(
    val cregs: HashMapE<VmStackValue>
) {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<VmSaveList> = VmSaveListTlbConstructor()
    }
}

private class VmSaveListTlbConstructor : TlbConstructor<VmSaveList>(
    schema = "_ cregs:(HashmapE 4 VmStackValue) = VmSaveList;"
) {
    private val vmStackValueCodec by lazy {
        VmStackValue.tlbCodec()
    }
    private val hashmapCombinator by lazy {
        HashMapE.tlbCodec(4, vmStackValueCodec)
    }

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: VmSaveList
    ) = cellBuilder {
        storeTlb(hashmapCombinator, value.cregs)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): VmSaveList = cellSlice {
        val creg = loadTlb(hashmapCombinator)
        VmSaveList(creg)
    }
}