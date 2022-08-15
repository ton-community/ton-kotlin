package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

@Serializable
@SerialName("fsm_split")
data class FutureSplitMergeSplit(
    val split_utime: UInt,
    val interval: UInt
) : FutureSplitMerge {
    companion object : TlbConstructorProvider<FutureSplitMergeSplit> by FutureSplitMergeSplitTlbConstructor
}

private object FutureSplitMergeSplitTlbConstructor : TlbConstructor<FutureSplitMergeSplit>(
    schema = "fsm_merge\$11 merge_utime:uint32 interval:uint32 = FutureSplitMerge;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: FutureSplitMergeSplit
    ) = cellBuilder {
        storeUInt32(value.split_utime)
        storeUInt32(value.interval)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): FutureSplitMergeSplit = cellSlice {
        val splitUtime = loadUInt32()
        val interval = loadUInt32()
        FutureSplitMergeSplit(splitUtime, interval)
    }
}
