package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.intellij.lang.annotations.Language
import org.ton.bitstring.BitString
import org.ton.cell.*
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@Serializable
@SerialName("shard_descr")
data class ShardDescr(
    val seq_no: Long,
    val reg_mc_seqno: Long,
    val start_lt: Long,
    val end_lt: Long,
    val root_hash: BitString,
    val file_hash: BitString,
    val before_split: Boolean,
    val before_merge: Boolean,
    val want_split: Boolean,
    val want_merge: Boolean,
    val nx_cc_updated: Boolean,
    val flags: Int,
    val next_catchain_seqno: Long,
    val next_validator_shard: Long,
    val min_ref_mc_seqno: Long,
    val gen_utime: Long,
    val split_merge_at: FutureSplitMerge,
    val fees_collected: CurrencyCollection,
    val funds_created: CurrencyCollection
) {
    init {
        require(root_hash.size == 256) { "required: root_hash.size == 256, actual: ${root_hash.size}" }
        require(file_hash.size == 256) { "required: file_hash.size == 256, actual: ${file_hash.size}" }
    }

    companion object {
        @JvmStatic
        fun tlbCodec(): TlbCombinator<ShardDescr> = ShardDescrTlbCombinator
    }
}

private object ShardDescrTlbCombinator : TlbCombinator<ShardDescr>() {
    val a = ShardDescrTlbConstructor(
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
    val b = ShardDescrTlbConstructor(
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

    override val constructors: List<TlbConstructor<out ShardDescr>> = listOf(
        a, b
    )

    override fun getConstructor(value: ShardDescr): TlbConstructor<out ShardDescr> = a
}

private class ShardDescrTlbConstructor(@Language("TL-B") schema: String) : TlbConstructor<ShardDescr>(
    schema
) {
    val referencedCurrencyCollection = id == BitString("a")
    val futureSplitMerge by lazy { FutureSplitMerge.tlbCodec() }
    val currencyCollection by lazy { CurrencyCollection.tlbCodec() }

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: ShardDescr
    ) = cellBuilder {
        storeUInt(value.seq_no, 32)
        storeUInt(value.reg_mc_seqno, 32)
        storeUInt(value.start_lt, 64)
        storeUInt(value.end_lt, 64)
        storeBits(value.root_hash)
        storeBits(value.file_hash)
        storeBit(value.before_split)
        storeBit(value.before_merge)
        storeBit(value.want_split)
        storeBit(value.want_merge)
        storeBit(value.nx_cc_updated)
        storeUInt(value.flags, 3)
        storeUInt(value.next_catchain_seqno, 32)
        storeUInt(value.next_validator_shard, 64)
        storeUInt(value.min_ref_mc_seqno, 32)
        storeUInt(value.gen_utime, 32)
        storeTlb(futureSplitMerge, value.split_merge_at)
        if (referencedCurrencyCollection) {
            storeRef {
                storeTlb(currencyCollection, value.fees_collected)
                storeTlb(currencyCollection, value.funds_created)
            }
        } else {
            storeTlb(currencyCollection, value.fees_collected)
            storeTlb(currencyCollection, value.funds_created)
        }
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): ShardDescr = cellSlice {
        val seqNo = loadUInt(32).toLong()
        val regMcSeqno = loadUInt(32).toLong()
        val startLt = loadUInt(64).toLong()
        val endLt = loadUInt(64).toLong()
        val rootHash = loadBits(256)
        val fileHash = loadBits(256)
        val beforeSplit = loadBit()
        val beforeMerge = loadBit()
        val wantSplit = loadBit()
        val wantMerge = loadBit()
        val nxCcUpdated = loadBit()
        val flags = loadUInt(3).toInt()
        val nextCatchainSeqno = loadUInt(32).toLong()
        val nextValidatorShard = loadUInt(64).toLong()
        val minRefMcSeqno = loadUInt(32).toLong()
        val genUtime = loadUInt(32).toLong()
        val splitMergeAt = loadTlb(futureSplitMerge)
        val (feesCollected, fundsCreated) = if (referencedCurrencyCollection) {
            loadRef {
                loadTlb(currencyCollection) to loadTlb(currencyCollection)
            }
        } else {
            loadTlb(currencyCollection) to loadTlb(currencyCollection)
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
