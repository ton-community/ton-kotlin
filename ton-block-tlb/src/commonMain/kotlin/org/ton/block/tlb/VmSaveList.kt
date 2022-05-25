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
        HashMapE.tlbCodec(vmStackValueCodec)
    }

    override fun encode(
        cellBuilder: CellBuilder,
        value: VmSaveList,
        param: Int,
        negativeParam: (Int) -> Unit
    ) = cellBuilder {
        storeTlb(value.cregs, hashmapCombinator, 4)
    }

    override fun decode(
        cellSlice: CellSlice,
        param: Int,
        negativeParam: (Int) -> Unit
    ): VmSaveList = cellSlice {
        val creg = loadTlb(hashmapCombinator, 4)
        VmSaveList(creg)
    }
}
