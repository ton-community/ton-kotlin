package org.ton.kotlin.shard

/**
 * Block counters with absolute value and rates.
 *
 * ```tlb
 * counters#_ last_updated:uint32 total:uint64 cnt2048:uint64 cnt65536:uint64 = Counters;
 * ```
 */
public data class BlockCounters(
    /**
     * Unix timestamp in seconds of the last counters update.
     */
    val lastUpdated: Long,

    /**
     * Total counter value.
     */
    val total: Long,

    /**
     * Scaled counter rate.
     */
    val cnt2048: Long,

    /**
     * Scaled counter rate (better precision).
     */
    val cnt65536: Long
)
