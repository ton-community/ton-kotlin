package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@Serializable
@SerialName("prev_blks_info")
data class PrevBlksInfo(
    val prev1: ExtBlkRef,
    val prev2: ExtBlkRef
) : BlkPrevInfo {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<PrevBlksInfo> = PrevBlksInfoTlbConstructor
    }
}

private object PrevBlksInfoTlbConstructor : TlbConstructor<PrevBlksInfo>(
    schema = "prev_blks_info\$_ prev1:^ExtBlkRef prev2:^ExtBlkRef = BlkPrevInfo 1;"
) {
    val extBlkRef by lazy { ExtBlkRef.tlbCodec() }

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: PrevBlksInfo
    ) = cellBuilder {
        storeRef { storeTlb(extBlkRef, value.prev1) }
        storeRef { storeTlb(extBlkRef, value.prev2) }
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): PrevBlksInfo = cellSlice {
        val prev1 = loadRef { loadTlb(extBlkRef) }
        val prev2 = loadRef { loadTlb(extBlkRef) }
        PrevBlksInfo(prev1, prev2)
    }
}