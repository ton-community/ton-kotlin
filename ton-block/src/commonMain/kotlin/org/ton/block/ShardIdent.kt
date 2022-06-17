package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SerialName("shard_ident")
@Serializable
data class ShardIdent(
    @SerialName("shard_pfx_bits")
    val shardPfxBits: Int,
    @SerialName("workchain_id")
    val workchainId: Int,
    @SerialName("shard_prefix")
    val shardPrefix: Long
) {
    init {
        require(shardPfxBits <= 60) { "expected: shard_pfx_bits <= 60, actual: $shardPfxBits" }
    }
}