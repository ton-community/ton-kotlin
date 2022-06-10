package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.hashmap.AugDictionary

@Serializable
@SerialName("block_create_stats_ext")
data class BlockCreateStatsExt(
    val counters: AugDictionary<CreatorStats, Long>
)
