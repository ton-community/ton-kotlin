package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.*
import org.ton.hashmap.AugDictionary
import org.ton.hashmap.HashMapE
import org.ton.tlb.TlbConstructor
import org.ton.tlb.asTlbCombinator
import org.ton.tlb.constructor.tlbCodec
import org.ton.tlb.loadTlb
import org.ton.tlb.providers.TlbCombinatorProvider
import org.ton.tlb.storeTlb

@Serializable
@SerialName("masterchain_state_extra")
data class McStateExtra(
    val shard_hashes: HashMapE<BinTree<ShardDescr>>,
    val config: ConfigParams,
    val flags: Int,
    val validator_info: ValidatorInfo,
    val prev_blocks: AugDictionary<KeyExtBlkRef, KeyMaxLt>,
    val after_key_block: Boolean,
    val last_key_block: Maybe<ExtBlkRef>,
    val block_create_stats: BlockCreateStats?,
    val global_balance: CurrencyCollection
) {
    companion object : TlbCombinatorProvider<McStateExtra> by McStateExtraTlbConstructor.asTlbCombinator()
}

private object McStateExtraTlbConstructor : TlbConstructor<McStateExtra>(
    schema = "masterchain_state_extra#cc26" +
            "  shard_hashes:ShardHashes" +
            "  config:ConfigParams" +
            "  ^[ flags:(## 16) { flags <= 1 }" +
            "     validator_info:ValidatorInfo" +
            "     prev_blocks:OldMcBlocksInfo" +
            "     after_key_block:Bool" +
            "     last_key_block:(Maybe ExtBlkRef)" +
            "     block_create_stats:(flags . 0)?BlockCreateStats ]" +
            "  global_balance:CurrencyCollection" +
            "= McStateExtra;"
) {
    val shardHashes = HashMapE.tlbCodec(32, Cell.tlbCodec(BinTree.tlbCodec(ShardDescr)))
    val oldMcBlocksInfo = AugDictionary.tlbCodec(32, KeyExtBlkRef, KeyMaxLt)
    val maybeExtBlkRef = Maybe.tlbCodec(ExtBlkRef)

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: McStateExtra
    ) = cellBuilder {
        storeTlb(shardHashes, value.shard_hashes)
        storeTlb(ConfigParams, value.config)
        storeRef {
            storeUInt(value.flags, 16)
            storeTlb(ValidatorInfo, value.validator_info)
            storeTlb(oldMcBlocksInfo, value.prev_blocks)
            storeBit(value.after_key_block)
            storeTlb(maybeExtBlkRef, value.last_key_block)
            if (value.block_create_stats != null) {
                storeTlb(BlockCreateStats, value.block_create_stats)
            }
        }
        storeTlb(CurrencyCollection, value.global_balance)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): McStateExtra = cellSlice {
        val shardHashes = loadTlb(shardHashes)
        val config = loadTlb(ConfigParams)
        val globalBalance = loadTlb(CurrencyCollection)
        loadRef {
            val flags = loadUInt(16).toInt()
            val validatorInfo = loadTlb(ValidatorInfo)
            val prevBlocks = loadTlb(oldMcBlocksInfo)
            val afterKeyBlock = loadBit()
            val lastKeyBlock = loadTlb(maybeExtBlkRef)
            val blockCreateStats = if (flags and 65536 == 65536) {
                loadTlb(BlockCreateStats)
            } else null
            McStateExtra(
                shardHashes,
                config,
                flags,
                validatorInfo,
                prevBlocks,
                afterKeyBlock,
                lastKeyBlock,
                blockCreateStats,
                globalBalance
            )
        }
    }
}
