package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.hashmap.HashMapE

@Serializable
@SerialName("block_create_stats")
data class BlockCreateStatsRegular(
    val counters: HashMapE<CreatorStats>
) : BlockCreateStats
