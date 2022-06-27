package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbConstructor

@Serializable
@SerialName("fsm_merge")
data class FutureSplitMergeMerge(
    val merge_utime: Long,
    val interval: Long
) : FutureSplitMerge {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<FutureSplitMergeMerge> = FutureSplitMergeMergeTlbConstructor
    }
}

private object FutureSplitMergeMergeTlbConstructor : TlbConstructor<FutureSplitMergeMerge>(
    schema = "fsm_merge\$11 merge_utime:uint32 interval:uint32 = FutureSplitMerge;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: FutureSplitMergeMerge
    ) = cellBuilder {
        storeUInt(value.merge_utime, 32)
        storeUInt(value.interval, 32)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): FutureSplitMergeMerge = cellSlice {
        val mergeUtime = loadUInt(32).toLong()
        val interval = loadUInt(32).toLong()
        FutureSplitMergeMerge(mergeUtime, interval)
    }
}