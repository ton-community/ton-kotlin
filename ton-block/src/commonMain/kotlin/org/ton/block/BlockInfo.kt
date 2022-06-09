package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SerialName("block_info")
@Serializable
data class BlockInfo(
    val version: Int,
    val not_master: Boolean,
    val after_merge: Boolean,
    val before_split: Boolean,
    val after_split: Boolean,
    val want_merge: Boolean,
    val key_block: Boolean,
    val vert_seqno_incr: Boolean,
    val flags: Int,
    val seq_no: Int,
    val vert_seq_no: Int,
    val shard: ShardIdent,
    val gen_utime: Int,
    val start_lt: Long,
    val end_lt: Long,
    val gen_validator_list_hash_short: Int,
    val gen_catchain_seqno: Int,
    val min_ref_mc_seqno: Int,
    val prev_key_block_seqno: Int,
    val gen_software: GlobalVersion?,
    val master_ref: BlkMasterInfo?,
    val prev_ref: BlkPrevInfo,
    val prev_vert_ref: BlkPrevInfo?
) {
    val prev_seq_no: Int get() = seq_no - 1

    init {
        require(flags <= 1) { "expected: flags <= 1, actual: $flags" }
        require(vert_seq_no >= 0 && vert_seqno_incr) { "expected: vert_seq_no >= vert_seqno_incr, actual: $vert_seq_no" }
    }
}