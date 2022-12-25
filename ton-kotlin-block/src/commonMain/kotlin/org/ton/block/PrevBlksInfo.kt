package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.*
import org.ton.tlb.CellRef
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb
import kotlin.jvm.JvmStatic

@Serializable
@SerialName("prev_blks_info")
public data class PrevBlksInfo(
    val prev1: CellRef<ExtBlkRef>,
    val prev2: CellRef<ExtBlkRef>
) : BlkPrevInfo {
    override fun prevs(): List<ExtBlkRef> = listOf(prev1.value, prev2.value)

    public companion object {
        @JvmStatic
        public fun tlbCodec(): TlbConstructor<PrevBlksInfo> = PrevBlksInfoTlbConstructor
    }
}

private object PrevBlksInfoTlbConstructor : TlbConstructor<PrevBlksInfo>(
    schema = "prev_blks_info\$_ prev1:^ExtBlkRef prev2:^ExtBlkRef = BlkPrevInfo 1;"
) {
    private val cellRef = CellRef.tlbCodec(ExtBlkRef)
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: PrevBlksInfo
    ) = cellBuilder {
        storeTlb(cellRef, value.prev1)
        storeTlb(cellRef, value.prev2)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): PrevBlksInfo = cellSlice {
        val prev1 = loadTlb(cellRef)
        val prev2 = loadTlb(cellRef)
        PrevBlksInfo(prev1, prev2)
    }
}
