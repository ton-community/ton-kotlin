package org.ton.block

import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.hashmap.HashMapE
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb
import kotlin.jvm.JvmStatic

@Serializable
public data class VmSaveList(
    val cregs: HashMapE<VmStackValue>
) {
    public companion object : TlbCodec<VmSaveList> by VmSaveListTlbConstructor {
        @JvmStatic
        public fun tlbCodec(): TlbConstructor<VmSaveList> = VmSaveListTlbConstructor
    }
}

private object VmSaveListTlbConstructor : TlbConstructor<VmSaveList>(
    schema = "_ cregs:(HashmapE 4 VmStackValue) = VmSaveList;"
) {
    private val hashmapCombinator = HashMapE.tlbCodec(4, VmStackValue)

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
