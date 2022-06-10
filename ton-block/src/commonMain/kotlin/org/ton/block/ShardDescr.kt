package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString

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
}
