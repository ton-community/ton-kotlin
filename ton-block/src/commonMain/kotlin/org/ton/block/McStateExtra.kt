package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("masterchain_state_extra")
data class McStateExtra(
    val shard_hashes: ShardHashes,
    val config: ConfigParams,
    val flags: Int,
    val validator_info: ValidatorInfo,
    val prev_blocks: OldMcBlocksInfo,
    val after_key_block: Boolean,
    val last_key_block: Maybe<ExtBlkRef>,
    val block_create_stats: BlockCreateStats?,
    val global_balance: CurrencyCollection
)
