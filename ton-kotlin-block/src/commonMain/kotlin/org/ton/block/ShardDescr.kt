package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.cell.*
import org.ton.tlb.*
import org.ton.tlb.providers.TlbCombinatorProvider

@Serializable
@SerialName("shard_descr")
data class ShardDescr(
    val seq_no: UInt,
    val reg_mc_seqno: UInt,
    val start_lt: ULong,
    val end_lt: ULong,
    val root_hash: BitString,
    val file_hash: BitString,
    val before_split: Boolean,
    val before_merge: Boolean,
    val want_split: Boolean,
    val want_merge: Boolean,
    val nx_cc_updated: Boolean,
    val flags: BitString,
    val next_catchain_seqno: UInt,
    val next_validator_shard: ULong,
    val min_ref_mc_seqno: UInt,
    val gen_utime: UInt,
    val split_merge_at: FutureSplitMerge,
    val fees_collected: CurrencyCollection,
    val funds_created: CurrencyCollection
) {
    init {
        require(root_hash.size == 256) { "required: root_hash.size == 256, actual: ${root_hash.size}" }
        require(file_hash.size == 256) { "required: file_hash.size == 256, actual: ${file_hash.size}" }
    }

    companion object : TlbCombinatorProvider<ShardDescr> by ShardDescrTlbCombinator
}

private object ShardDescrTlbCombinator : TlbCombinator<ShardDescr>(
    ShardDescr::class,
    ShardDescr::class to a,
    ShardDescr::class to b,
) {
    override fun findTlbStorerOrNull(value: ShardDescr) = a
}

private val a = ShardDescrTlbConstructor(
    "shard_descr_new#a seq_no:uint32 reg_mc_seqno:uint32 " +
            "  start_lt:uint64 end_lt:uint64 " +
            "  root_hash:bits256 file_hash:bits256 " +
            "  before_split:Bool before_merge:Bool " +
            "  want_split:Bool want_merge:Bool " +
            "  nx_cc_updated:Bool flags:(## 3) { flags = 0 } " +
            "  next_catchain_seqno:uint32 next_validator_shard:uint64 " +
            "  min_ref_mc_seqno:uint32 gen_utime:uint32 " +
            "  split_merge_at:FutureSplitMerge " +
            "  ^[ fees_collected:CurrencyCollection " +
            "     funds_created:CurrencyCollection ] = ShardDescr;"
)
private val b = ShardDescrTlbConstructor(
    "shard_descr#b seq_no:uint32 reg_mc_seqno:uint32 " +
            "  start_lt:uint64 end_lt:uint64 " +
            "  root_hash:bits256 file_hash:bits256 " +
            "  before_split:Bool before_merge:Bool " +
            "  want_split:Bool want_merge:Bool " +
            "  nx_cc_updated:Bool flags:(## 3) { flags = 0 } " +
            "  next_catchain_seqno:uint32 next_validator_shard:uint64 " +
            "  min_ref_mc_seqno:uint32 gen_utime:uint32 " +
            "  split_merge_at:FutureSplitMerge " +
            "  fees_collected:CurrencyCollection " +
            "  funds_created:CurrencyCollection = ShardDescr;"
)

private class ShardDescrTlbConstructor(schema: String) : TlbConstructor<ShardDescr>(
    schema
) {
    val referencedCurrencyCollection = id == BitString("a")

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: ShardDescr
    ) = cellBuilder {
        storeUInt32(value.seq_no)
        storeUInt32(value.reg_mc_seqno)
        storeUInt64(value.start_lt)
        storeUInt64(value.end_lt)
        storeBits(value.root_hash)
        storeBits(value.file_hash)
        storeBit(value.before_split)
        storeBit(value.before_merge)
        storeBit(value.want_split)
        storeBit(value.want_merge)
        storeBit(value.nx_cc_updated)
        storeBits(value.flags)
        storeUInt32(value.next_catchain_seqno)
        storeUInt64(value.next_validator_shard)
        storeUInt32(value.min_ref_mc_seqno)
        storeUInt32(value.gen_utime)
        storeTlb(FutureSplitMerge, value.split_merge_at)
        if (referencedCurrencyCollection) {
            storeRef {
                storeTlb(CurrencyCollection, value.fees_collected)
                storeTlb(CurrencyCollection, value.funds_created)
            }
        } else {
            storeTlb(CurrencyCollection, value.fees_collected)
            storeTlb(CurrencyCollection, value.funds_created)
        }
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): ShardDescr = cellSlice {
        val seqNo = loadUInt32()
        val regMcSeqno = loadUInt32()
        val startLt = loadUInt64()
        val endLt = loadUInt64()
        val rootHash = loadBits(256)
        val fileHash = loadBits(256)
        val beforeSplit = loadBit()
        val beforeMerge = loadBit()
        val wantSplit = loadBit()
        val wantMerge = loadBit()
        val nxCcUpdated = loadBit()
        val flags = loadBits(3)
        val nextCatchainSeqno = loadUInt32()
        val nextValidatorShard = loadUInt64()
        val minRefMcSeqno = loadUInt32()
        val genUtime = loadUInt32()
        val splitMergeAt = loadTlb(FutureSplitMerge)
        val (feesCollected, fundsCreated) = if (referencedCurrencyCollection) {
            loadRef {
                loadTlb(CurrencyCollection) to loadTlb(CurrencyCollection)
            }
        } else {
            loadTlb(CurrencyCollection) to loadTlb(CurrencyCollection)
        }
        ShardDescr(
            seqNo,
            regMcSeqno,
            startLt,
            endLt,
            rootHash,
            fileHash,
            beforeSplit,
            beforeMerge,
            wantSplit,
            wantMerge,
            nxCcUpdated,
            flags,
            nextCatchainSeqno,
            nextValidatorShard,
            minRefMcSeqno,
            genUtime,
            splitMergeAt,
            feesCollected,
            fundsCreated
        )
    }
}
