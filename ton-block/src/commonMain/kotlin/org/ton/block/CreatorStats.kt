package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("creator_info")
data class CreatorStats(
    val mc_blocks: Counters,
    val shard_blocks: Counters
)
