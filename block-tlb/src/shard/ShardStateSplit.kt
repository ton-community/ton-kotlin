package org.ton.kotlin.shard

public data class ShardStateSplit(
    val left: ShardStateUnsplit,
    val right: ShardStateUnsplit
) : ShardState