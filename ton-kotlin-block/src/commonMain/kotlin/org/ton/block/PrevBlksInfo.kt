package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.*
import org.ton.tlb.CellRef
import org.ton.tlb.TlbConstructor
import kotlin.jvm.JvmStatic

@Serializable
@SerialName("prev_blks_info")
data class PrevBlksInfo(
    val prev1: CellRef<ExtBlkRef>,
    val prev2: CellRef<ExtBlkRef>
) : BlkPrevInfo {
    override fun prevs(): List<ExtBlkRef> = listOf(prev1.value, prev2.value)

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
        storeRef(value.prev1.cell)
        storeRef(value.prev2.cell)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): PrevBlksInfo = cellSlice {
        val prev1 = CellRef(loadRef(), ExtBlkRef)
        val prev2 = CellRef(loadRef(), ExtBlkRef)
        PrevBlksInfo(prev1, prev2)
    }
}
