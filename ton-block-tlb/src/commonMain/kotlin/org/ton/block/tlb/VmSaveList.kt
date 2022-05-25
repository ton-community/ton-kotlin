package org.ton.block.tlb

import org.ton.block.VmSaveList
import org.ton.block.VmStackValue
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.hashmap.HashMapE
import org.ton.hashmap.tlb.tlbCodec
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

fun VmSaveList.Companion.tlbCodec(): TlbCodec<VmSaveList> = VmSaveListTlbConstructor()

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
