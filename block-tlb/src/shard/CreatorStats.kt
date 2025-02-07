package org.ton.kotlin.shard

/**
 * Block production statistics for the single validator.
 */
public data class CreatorStats(
    /**
     * Masterchain block production statistics.
     */
    val mcBlocks: BlockCounters,

    /**
     * Block production statistics for other workchains.
     */
    val shardBlocks: BlockCounters,
)
