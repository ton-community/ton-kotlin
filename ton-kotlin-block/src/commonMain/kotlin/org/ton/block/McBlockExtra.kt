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
@SerialName("masterchain_block_extra")
data class McBlockExtra(
    val key_block: Boolean,
    val shard_hashes: HashMapE<BinTree<ShardDescr>>,
    val shard_fees: AugDictionary<ShardFeeCreated, ShardFeeCreated>,
    val prev_blk_signatures: HashMapE<CryptoSignaturePair>,
    val recover_create_msg: Maybe<InMsg>,
    val mint_msg: Maybe<InMsg>,
    val config: ConfigParams?
) {
    companion object : TlbCombinatorProvider<McBlockExtra> by McBlockExtraTlbConstructor.asTlbCombinator()
}

private object McBlockExtraTlbConstructor : TlbConstructor<McBlockExtra>(
    schema = "masterchain_block_extra#cca5 " +
            "key_block:(## 1) " +
            "shard_hashes:ShardHashes " +
            "shard_fees:ShardFees " +
            "^[ prev_blk_signatures:(HashmapE 16 CryptoSignaturePair) " +
            "   recover_create_msg:(Maybe ^InMsg) " +
            "   mint_msg:(Maybe ^InMsg) ] " +
            "config:key_block?ConfigParams " +
            "= McBlockExtra;"
) {
    val shardHashes = HashMapE.tlbCodec(32, Cell.tlbCodec(BinTree.tlbCodec(ShardDescr)))
    val shardFees = AugDictionary.tlbCodec(96, ShardFeeCreated, ShardFeeCreated)
    val hashmapCryptoSignaturePair = HashMapE.tlbCodec(16, CryptoSignaturePair)
    val maybeInMsg = Maybe.tlbCodec(Cell.tlbCodec(InMsg))

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: McBlockExtra
    ) = cellBuilder {
        storeBit(value.key_block)
        storeTlb(shardHashes, value.shard_hashes)
        storeTlb(shardFees, value.shard_fees)
        storeRef {
            storeTlb(hashmapCryptoSignaturePair, value.prev_blk_signatures)
            storeTlb(maybeInMsg, value.recover_create_msg)
            storeTlb(maybeInMsg, value.mint_msg)
        }
        if (value.key_block && value.config != null) {
            storeTlb(ConfigParams, value.config)
        }
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): McBlockExtra = cellSlice {
        val keyBlock = loadBit()
        val shardHashes = loadTlb(shardHashes)
        val shardFees = loadTlb(shardFees)
        val config = if (keyBlock) {
            loadTlb(ConfigParams)
        } else null
        loadRef {
            val prevBlkSignatures = loadTlb(hashmapCryptoSignaturePair)
            val recoverCreateMsg = loadTlb(maybeInMsg)
            val mintMsg = loadTlb(maybeInMsg)
            McBlockExtra(keyBlock, shardHashes, shardFees, prevBlkSignatures, recoverCreateMsg, mintMsg, config)
        }
    }
}
