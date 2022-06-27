package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.*
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
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: PrevBlksInfo
    ) = cellBuilder {
        storeRef { storeTlb(ExtBlkRef, value.prev1) }
        storeRef { storeTlb(ExtBlkRef, value.prev2) }
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): PrevBlksInfo = cellSlice {
        val prev1 = loadRef { loadTlb(ExtBlkRef) }
        val prev2 = loadRef { loadTlb(ExtBlkRef) }
        PrevBlksInfo(prev1, prev2)
    }
}