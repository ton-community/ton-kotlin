package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor

@Serializable
@SerialName("split_merge_info")
data class SplitMergeInfo(
    val cur_shard_pfx_len: Int,
    val acc_split_depth: Int,
    val this_addr: BitString,
    val sibling_addr: BitString
) {
    init {
        require(this_addr.size == 256) { "required: this_addr.size == 256, actual: ${this_addr.size}" }
        require(sibling_addr.size == 256) { "required: sibling_addr.size == 256, actual: ${sibling_addr.size}" }
    }

    companion object : TlbCodec<SplitMergeInfo> by SplitMergeInfoTlbConstructor
}

private object SplitMergeInfoTlbConstructor : TlbConstructor<SplitMergeInfo>(
    schema = "split_merge_info\$_ cur_shard_pfx_len:(## 6)\n" +
            "  acc_split_depth:(## 6) this_addr:bits256 sibling_addr:bits256\n" +
            "  = SplitMergeInfo;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: SplitMergeInfo
    ) = cellBuilder {
        storeUInt(value.cur_shard_pfx_len, 6)
        storeUInt(value.acc_split_depth, 6)
        storeBits(value.this_addr)
        storeBits(value.sibling_addr)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): SplitMergeInfo = cellSlice {
        val curShardPfxLen = loadUInt(6).toInt()
        val accSplitDepth = loadUInt(6).toInt()
        val thisAddr = loadBits(256)
        val siblingAddr = loadBits(256)
        SplitMergeInfo(curShardPfxLen, accSplitDepth, thisAddr, siblingAddr)
    }
}
