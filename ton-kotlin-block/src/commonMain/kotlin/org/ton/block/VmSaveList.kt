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
data class VmSaveList(
    val cregs: HashMapE<VmStackValue> = HashMapE.of()
) {
    private val entries by lazy(LazyThreadSafetyMode.PUBLICATION) {
        cregs.nodes().map {
            CellSlice(it.first).loadTinyInt(4).toInt() to it.second
        }.toMap()
    }

    val c7: VmStackTuple get() = get(7) as VmStackTuple
    val c4: VmStackCell get() = (get(4) as VmStackCell)

    public operator fun get(index: Int): VmStackValue? = entries[index]

    companion object : TlbCodec<VmSaveList> by VmSaveListTlbConstructor {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<VmSaveList> = VmSaveListTlbConstructor
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
