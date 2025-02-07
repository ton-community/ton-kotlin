package org.ton.kotlin.shard

import org.ton.kotlin.block.BlockRef
import org.ton.kotlin.config.BlockchainConfigParams
import org.ton.kotlin.currency.CurrencyCollection
import org.ton.kotlin.dict.RawDictionary

/**
 * Additional content for masterchain state.
 *
 * ```tlb
 * masterchain_state_extra#cc26
 *   shard_hashes:ShardHashes
 *   config:ConfigParams
 *   ^[ flags:(## 16) { flags <= 1 }
 *      validator_info:ValidatorInfo
 *      prev_blocks:OldMcBlocksInfo
 *      after_key_block:Bool
 *      last_key_block:(Maybe ExtBlkRef)
 *      block_create_stats:(flags . 0)?BlockCreateStats ]
 *   global_balance:CurrencyCollection
 * = McStateExtra;
 * ```
 */
public data class McStateExtra(
    /**
     * A tree of the most recent descriptions for all currently existing shards for all workchains except the masterchain.
     */
    val shards: ShardHashes,

    /**
     * The most recent blockchain config (if the block is a key block).
     */
    val config: BlockchainConfigParams,

    /**
     * Brief validator info.
     */
    val validatorInfo: ValidatorInfo,

    /**
     * A dictionary with previous masterchain blocks.
     */
    val prevBlocks: RawDictionary,

    /**
     * Whether this state was produced after the key block.
     */
    val afterKeyBlock: Boolean,

    /**
     * Optional reference to the latest known key block.
     */
    val lastKeyBlock: BlockRef?,

    /**
     * Block creation stats for validators from the current set.
     */
    val blockCreateStats: BlockCreateStats?,

    /**
     * Total balance of all accounts.
     */
    val globalBalance: CurrencyCollection
)

/*
    public object Tlb : TlbCodec<McStateExtra> {
        private const val TAG = 0xcc26
        private const val BLOCK_STATS_TAG = 0x17
        private val prevBlocksCodec = HashmapAugE.tlbCodec(32, KeyExtBlkRef, KeyMaxLt)
        private val statsConfig = HashMapE.tlbCodec(256, CreatorStats.Tlb)

        override fun storeTlb(cellBuilder: CellBuilder, value: McStateExtra): Unit = cellBuilder {
            storeUInt(TAG, 16)
            ShardHashes.Tlb.storeTlb(this, value.shards)
            ConfigParams.Tlb.storeTlb(this, value.config)
            storeRef {
                val flags = if (value.blockCreateStats != null) 1 else 0
                storeUInt(flags, 16)
                ValidatorInfo.Tlb.storeTlb(this, value.validatorInfo)
                prevBlocksCodec.storeTlb(this, value.prevBlocks)
                storeBoolean(value.afterKeyBlock)
                storeNullableTlb(BlockRef.Tlb, value.lastKeyBlock)
                if (value.blockCreateStats != null) {
                    storeUInt(BLOCK_STATS_TAG, 8)
                    statsConfig.storeTlb(this, value.blockCreateStats)
                }
            }
            CurrencyCollection.Tlb.storeTlb(this, value.globalBalance)
        }

        override fun loadTlb(cellSlice: CellSlice): McStateExtra = cellSlice {
            check(TAG == loadUInt(16).toInt()) {
                "Invalid tag"
            }
            val shards = ShardHashes.Tlb.loadTlb(this)
            val config = ConfigParams.Tlb.loadTlb(this)
            val ref = loadRef().beginParse()
            val flags = ref.loadUInt(16).toInt()
            val validatorInfo = ValidatorInfo.Tlb.loadTlb(ref)
            val prevBlocks = prevBlocksCodec.loadTlb(ref)
            val afterKeyBlock = ref.loadBit()
            val lastKeyBlock = ref.loadNullableTlb(BlockRef.Tlb)
            val blockCreateStats = if (flags and 1 != 0) {
                check(BLOCK_STATS_TAG == loadUInt(8).toInt()) { "Invalid tag" }
                statsConfig.loadTlb(ref)
            } else null
            val globalBalance = CurrencyCollection.Tlb.loadTlb(this)

            McStateExtra(
                shards,
                config,
                validatorInfo,
                prevBlocks,
                afterKeyBlock,
                lastKeyBlock,
                blockCreateStats,
                globalBalance
            )
        }
    }
 */



