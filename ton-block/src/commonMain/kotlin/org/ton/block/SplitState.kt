package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("split_state")
data class SplitState(
    val left: ShardStateUnsplit,
    val right: ShardStateUnsplit
) : ShardState
