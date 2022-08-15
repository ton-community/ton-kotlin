package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

@Serializable
@SerialName("fsm_merge")
data class FutureSplitMergeMerge(
    val merge_utime: UInt,
    val interval: UInt
) : FutureSplitMerge {
    companion object : TlbConstructorProvider<FutureSplitMergeMerge> by FutureSplitMergeMergeTlbConstructor
}

private object FutureSplitMergeMergeTlbConstructor : TlbConstructor<FutureSplitMergeMerge>(
    schema = "fsm_merge\$11 merge_utime:uint32 interval:uint32 = FutureSplitMerge;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: FutureSplitMergeMerge
    ) = cellBuilder {
        storeUInt32(value.merge_utime)
        storeUInt32(value.interval)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): FutureSplitMergeMerge = cellSlice {
        val mergeUtime = loadUInt32()
        val interval = loadUInt32()
        FutureSplitMergeMerge(mergeUtime, interval)
    }
}
