package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.hashmap.AugDictionary
import org.ton.hashmap.HashMapE
import org.ton.tlb.TlbConstructor
import org.ton.tlb.constructor.tlbCodec
import org.ton.tlb.loadTlb
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
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<McStateExtra> = McStateExtraTlbConstructor
    }
}

private object McStateExtraTlbConstructor : TlbConstructor<McStateExtra>(
    schema = "masterchain_state_extra#cc26\n" +
            "  shard_hashes:ShardHashes\n" +
            "  config:ConfigParams\n" +
            "  ^[ flags:(## 16) { flags <= 1 }\n" +
            "     validator_info:ValidatorInfo\n" +
            "     prev_blocks:OldMcBlocksInfo\n" +
            "     after_key_block:Bool\n" +
            "     last_key_block:(Maybe ExtBlkRef)\n" +
            "     block_create_stats:(flags . 0)?BlockCreateStats ]\n" +
            "  global_balance:CurrencyCollection\n" +
            "= McStateExtra;"
) {
    val shardHashes by lazy { HashMapE.tlbCodec(32, Cell.tlbCodec(BinTree.tlbCodec(ShardDescr.tlbCodec()))) }
    val configParams by lazy { ConfigParams.tlbCodec() }
    val validatorInfo by lazy { ValidatorInfo.tlbCodec() }
    val oldMcBlocksInfo by lazy { AugDictionary.tlbCodec(32, KeyExtBlkRef.tlbCodec(), KeyMaxLt.tlbCodec()) }
    val maybeExtBlkRef by lazy { Maybe.tlbCodec(ExtBlkRef.tlbCodec()) }
    val blockCreateStats by lazy { BlockCreateStats.tlbCodec() }
    val currencyCollection by lazy { CurrencyCollection.tlbCodec() }

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: McStateExtra
    ) = cellBuilder {
        storeTlb(shardHashes, value.shard_hashes)
        storeTlb(configParams, value.config)
        storeRef {
            storeUInt(value.flags, 16)
            storeTlb(validatorInfo, value.validator_info)
            storeTlb(oldMcBlocksInfo, value.prev_blocks)
            storeBit(value.after_key_block)
            storeTlb(maybeExtBlkRef, value.last_key_block)
            if (value.block_create_stats != null) {
                storeTlb(blockCreateStats, value.block_create_stats)
            }
        }
        storeTlb(currencyCollection, value.global_balance)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): McStateExtra = cellSlice {
        val shardHashes = loadTlb(shardHashes)
        val config = loadTlb(configParams)
        val globalBalance = loadTlb(currencyCollection)
        loadRef {
            val flags = loadUInt(16).toInt()
            val validatorInfo = loadTlb(validatorInfo)
            val prevBlocks = loadTlb(oldMcBlocksInfo)
            val afterKeyBlock = loadBit()
            val lastKeyBlock = loadTlb(maybeExtBlkRef)
            val blockCreateStats = if (flags and 65536 == 65536) {
                loadTlb(blockCreateStats)
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
