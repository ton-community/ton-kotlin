package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.bitstring.toBitString
import org.ton.cell.*
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@SerialName("block_info")
@Serializable
data class BlockInfo(
    val version: UInt,
    val not_master: Boolean,
    val after_merge: Boolean,
    val before_split: Boolean,
    val after_split: Boolean,
    val want_split: Boolean,
    val want_merge: Boolean,
    val key_block: Boolean,
    val vert_seqno_incr: Boolean,
    val flags: BitString,
    val seq_no: UInt,
    val vert_seq_no: UInt,
    val shard: ShardIdent,
    val gen_utime: UInt,
    val start_lt: ULong,
    val end_lt: ULong,
    val gen_validator_list_hash_short: UInt,
    val gen_catchain_seqno: UInt,
    val min_ref_mc_seqno: UInt,
    val prev_key_block_seqno: UInt,
    val gen_software: GlobalVersion?,
    val master_ref: BlkMasterInfo?,
    val prev_ref: BlkPrevInfo,
    val prev_vert_ref: BlkPrevInfo?
) {
    val prev_seq_no: UInt get() = seq_no - 1u

    init {
        require(flags.size == 8) { "expected: flags.size == 8, actual: ${flags.size}" }
        require(flags.subList(1, flags.lastIndex).all { !it }) { "expected: flags <= 1, actual: $flags" }
        require(vert_seq_no >= 0u) { "expected: vert_seq_no >= vert_seqno_incr, actual: $vert_seq_no" }
    }

    companion object : TlbCodec<BlockInfo> by BlockInfoTlbConstructor.asTlbCombinator()
}

private object BlockInfoTlbConstructor : TlbConstructor<BlockInfo>(
    schema = "block_info#9bc7a987 version:uint32 " +
        "not_master:(## 1) " +
        "after_merge:(## 1) before_split:(## 1) " +
        "after_split:(## 1) " +
        "want_split:Bool want_merge:Bool " +
        "key_block:Bool vert_seqno_incr:(## 1) " +
        "flags:(## 8) { flags <= 1 } " +
        "seq_no:# vert_seq_no:# { vert_seq_no >= vert_seqno_incr } " +
        "{ prev_seq_no:# } { ~prev_seq_no + 1 = seq_no } " +
        "shard:ShardIdent gen_utime:uint32 " +
        "start_lt:uint64 end_lt:uint64 " +
        "gen_validator_list_hash_short:uint32 " +
        "gen_catchain_seqno:uint32 " +
        "min_ref_mc_seqno:uint32 " +
        "prev_key_block_seqno:uint32 " +
        "gen_software:flags . 0?GlobalVersion " +
        "master_ref:not_master?^BlkMasterInfo " +
        "prev_ref:^(BlkPrevInfo after_merge) " +
        "prev_vert_ref:vert_seqno_incr?^(BlkPrevInfo 0) " +
        "= BlockInfo;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: BlockInfo
    ) = cellBuilder {
        storeUInt32(value.version)
        storeBit(value.not_master)
        storeBit(value.after_merge)
        storeBit(value.before_split)
        storeBit(value.after_split)
        storeBit(value.want_split)
        storeBit(value.want_merge)
        storeBit(value.key_block)
        storeBit(value.vert_seqno_incr)
        storeBits(value.flags.asReversed())
        storeUInt32(value.seq_no)
        storeUInt32(value.vert_seq_no)
        storeTlb(ShardIdent, value.shard)
        storeUInt32(value.gen_utime)
        storeUInt64(value.start_lt)
        storeUInt64(value.end_lt)
        storeUInt32(value.gen_validator_list_hash_short)
        storeUInt32(value.gen_catchain_seqno)
        storeUInt32(value.min_ref_mc_seqno)
        storeUInt32(value.prev_key_block_seqno)
        if (value.flags[0] && value.gen_software != null) {
            storeTlb(GlobalVersion, value.gen_software)
        }
        if (value.not_master && value.master_ref != null) {
            storeRef {
                storeTlb(BlkMasterInfo, value.master_ref)
            }
        }
        storeRef {
            storeTlb(BlkPrevInfo.tlbCodec(value.after_merge), value.prev_ref)
        }
        if (value.vert_seqno_incr && value.prev_vert_ref != null) {
            storeRef {
                storeTlb(BlkPrevInfo.tlbCodec(0), value.prev_vert_ref)
            }
        }
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): BlockInfo = cellSlice {
        val version = loadUInt32()
        val notMaster = loadBit()
        val afterMerge = loadBit()
        val beforeSplit = loadBit()
        val afterSplit = loadBit()
        val wantSplit = loadBit()
        val wantMerge = loadBit()
        val keyBlock = loadBit()
        val verSeqnoIncr = loadBit()
        val flags = loadBits(8).asReversed().toBitString()
        val seqNo = loadUInt32()
        val vertSeqNo = loadUInt32()
        val shard = loadTlb(ShardIdent)
        val genUtime = loadUInt32()
        val startLt = loadUInt64()
        val endLt = loadUInt64()
        val genValidatorListHashShort = loadUInt32()
        val genCatchainSeqno = loadUInt32()
        val minRefMcSeqno = loadUInt32()
        val prevKeyBlockSeqno = loadUInt32()
        val genSoftware = if (flags[0]) {
            loadTlb(GlobalVersion)
        } else null
        val masterRef = if (notMaster) {
            loadRef { loadTlb(BlkMasterInfo) }
        } else null
        val prevRef = loadRef { loadTlb(BlkPrevInfo.tlbCodec(afterMerge)) }
        val prevVertRef = if (verSeqnoIncr) {
            loadRef { loadTlb(BlkPrevInfo.tlbCodec(0)) }
        } else null
        BlockInfo(
            version,
            notMaster,
            afterMerge,
            beforeSplit,
            afterSplit,
            wantSplit,
            wantMerge,
            keyBlock,
            verSeqnoIncr,
            flags,
            seqNo,
            vertSeqNo,
            shard,
            genUtime,
            startLt,
            endLt,
            genValidatorListHashShort,
            genCatchainSeqno,
            minRefMcSeqno,
            prevKeyBlockSeqno,
            genSoftware,
            masterRef,
            prevRef,
            prevVertRef
        )
    }
}
