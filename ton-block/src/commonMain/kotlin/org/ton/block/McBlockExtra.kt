package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.hashmap.HashMapE

@Serializable
@SerialName("masterchain_block_extra")
data class McBlockExtra(
    val key_block: Boolean,
    val shard_hashes: ShardHashes,
    val shard_fees: ShardFees,
    val prev_blk_signatures: HashMapE<CryptoSignaturePair>,
    val recover_create_msg: Maybe<InMsg>,
    val mint_msg: Maybe<InMsg>,
    val config: ConfigParams?
)