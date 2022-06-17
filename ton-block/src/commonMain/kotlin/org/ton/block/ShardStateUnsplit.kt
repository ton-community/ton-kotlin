package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.hashmap.HashMapE

@Serializable
@SerialName("shard_state")
data class ShardStateUnsplit(
    val global_id: Int,
    val shard_id: ShardIdent,
    val seq_no: Long,
    val vert_seq_no: Int,
    val gen_utime: Long,
    val gen_lt: Long,
    val min_ref_mc_seqno: Long,
    val out_msg_queue_info: OutMsgQueueInfo,
    val before_split: Boolean,
    val accounts: ShardAccounts,
    val overload_history: Long,
    val underload_history: Long,
    val total_balance: CurrencyCollection,
    val total_validator_fees: CurrencyCollection,
    val libraries: HashMapE<LibDescr>,
    val master_ref: Maybe<BlkMasterInfo>,
    val custom: Maybe<McStateExtra>
) : ShardState
