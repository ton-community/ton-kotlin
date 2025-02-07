package org.ton.kotlin.block

import org.ton.kotlin.cell.CellBuilder
import org.ton.kotlin.cell.CellContext
import org.ton.kotlin.cell.CellSlice
import org.ton.kotlin.cell.serialization.CellSerializer
import org.ton.kotlin.shard.ShardIdent

/**
 * Block info.
 */
public data class BlockInfo(
    /**
     * Block model version.
     */
    val version: Int,

    /**
     * Whether this block was produced after the shards were merged.
     */
    val afterMerge: Boolean,

    /**
     * Whether this block was produced before the shards split.
     */
    val beforeSplit: Boolean,

    /**
     * Whether this block was produced after the shards split.
     */
    val afterSplit: Boolean,

    /**
     * Hint that the shard with this block should split.
     */
    val wantSplit: Boolean,

    /**
     * Hint that the shard with this block should merge.
     */
    val wantMerge: Boolean,

    /**
     * Whether this block is a key block.
     */
    val keyBlock: Boolean,

    /**
     * Block flags (currently only bit 1 is used, for [genSoftware])
     */
    val flags: Int,

    /**
     * Block sequence number.
     */
    val seqno: Int,

    /**
     * Block vertical sequence number.
     */
    val vertSeqno: Int,

    /**
     * Shard id where this block was produced.
     */
    val shard: ShardIdent,

    /**
     * Unix timestamp in seconds when the block was created.
     */
    val genTime: Long,

    /**
     * Logical time range start.
     */
    val startLt: Long,

    /**
     * Logical time range end.
     */
    val endLt: Long,

    /**
     * Last 4 bytes of the hash of the validator list.
     */
    val genValidatorListHashShort: Int,

    /**
     * Seqno of the catchain session where this block was produced.
     */
    val genCatChainSeqno: Int,

    /**
     * Minimal referenced seqno of the masterchain block.
     */
    val minRefMcSeqno: Int,

    /**
     * Previous key block seqno.
     */
    val prevKeyBlockSeqno: Int,

    val genSoftware: GlobalVersion
) {
    public companion object : CellSerializer<BlockInfo> by BlockInfoSerializer
}

private object BlockInfoSerializer : CellSerializer<BlockInfo> {
    override fun load(
        slice: CellSlice,
        context: CellContext
    ): BlockInfo {
        TODO("Not yet implemented")
    }

    override fun store(
        builder: CellBuilder,
        value: BlockInfo,
        context: CellContext
    ) {
        TODO("Not yet implemented")
    }

}