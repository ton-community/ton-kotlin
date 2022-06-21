package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@Serializable
@SerialName("prev_blk_info")
data class PrevBlkInfo(
    val prev: ExtBlkRef
) : BlkPrevInfo {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<PrevBlkInfo> = PrevBlkInfoTlbConstructor
    }
}

private object PrevBlkInfoTlbConstructor : TlbConstructor<PrevBlkInfo>(
    schema = "prev_blk_info\$_ prev:ExtBlkRef = BlkPrevInfo 0;"
) {
    val extBlkRef by lazy { ExtBlkRef.tlbCodec() }

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: PrevBlkInfo
    ) = cellBuilder {
        storeTlb(extBlkRef, value.prev)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): PrevBlkInfo = cellSlice {
        val prev = loadTlb(extBlkRef)
        PrevBlkInfo(prev)
    }
}