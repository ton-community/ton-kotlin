package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SerialName("block")
@Serializable
data class Block(
    val global_id: Int,
    val info: BlockInfo,
    val value_flow: ValueFlow,
    val state_update: MerkleUpdate<ShardState>,
    val extra: BlockExtra
)