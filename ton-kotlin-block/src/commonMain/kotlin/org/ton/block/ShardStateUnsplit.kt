package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.*
import org.ton.hashmap.AugDictionary
import org.ton.hashmap.HashMapE
import org.ton.tlb.TlbConstructor
import org.ton.tlb.constructor.tlbCodec
import org.ton.tlb.loadTlb
import org.ton.tlb.providers.TlbConstructorProvider
import org.ton.tlb.storeTlb

@Serializable
@SerialName("shard_state")
data class ShardStateUnsplit(
    val global_id: UInt,
    val shard_id: ShardIdent,
    val seq_no: UInt,
    val vert_seq_no: UInt,
    val gen_utime: UInt,
    val gen_lt: ULong,
    val min_ref_mc_seqno: UInt,
    val out_msg_queue_info: OutMsgQueueInfo,
    val before_split: Boolean,
    val accounts: AugDictionary<ShardAccount, DepthBalanceInfo>,
    val overload_history: ULong,
    val underload_history: ULong,
    val total_balance: CurrencyCollection,
    val total_validator_fees: CurrencyCollection,
    val libraries: HashMapE<LibDescr>,
    val master_ref: Maybe<BlkMasterInfo>,
    val custom: Maybe<McStateExtra>
) : ShardState {
    companion object : TlbConstructorProvider<ShardStateUnsplit> by ShardStateUnsplitTlbConstructor
}

private object ShardStateUnsplitTlbConstructor : TlbConstructor<ShardStateUnsplit>(
    schema = "shard_state#9023afe2 global_id:int32 " +
            "shard_id:ShardIdent " +
            "seq_no:uint32 vert_seq_no:# " +
            "gen_utime:uint32 gen_lt:uint64 " +
            "min_ref_mc_seqno:uint32 " +
            "out_msg_queue_info:^OutMsgQueueInfo " +
            "before_split:(## 1) " +
            "accounts:^ShardAccounts " +
            "^[ overload_history:uint64 underload_history:uint64 " +
            "total_balance:CurrencyCollection " +
            "total_validator_fees:CurrencyCollection " +
            "libraries:(HashmapE 256 LibDescr) " +
            "master_ref:(Maybe BlkMasterInfo) ] " +
            "custom:(Maybe ^McStateExtra) " +
            "= ShardStateUnsplit;"
) {
    val shardAccounts = AugDictionary.tlbCodec(256, ShardAccount, DepthBalanceInfo)
    val libraries = HashMapE.tlbCodec(256, LibDescr)
    val maybeBlkMasterInfo = Maybe.tlbCodec(BlkMasterInfo)
    val maybeMcStateExtra = Maybe.tlbCodec(Cell.tlbCodec(McStateExtra))

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: ShardStateUnsplit
    ) = cellBuilder {
        storeUInt32(value.global_id)
        storeTlb(ShardIdent, value.shard_id)
        storeUInt32(value.seq_no)
        storeUInt32(value.vert_seq_no)
        storeUInt32(value.min_ref_mc_seqno)
        storeRef {
            storeTlb(OutMsgQueueInfo, value.out_msg_queue_info)
        }
        storeBits(value.before_split)
        storeRef {
            storeTlb(shardAccounts, value.accounts)
        }
        storeRef {
            storeUInt64(value.overload_history)
            storeUInt64(value.underload_history)
            storeTlb(CurrencyCollection, value.total_balance)
            storeTlb(CurrencyCollection, value.total_validator_fees)
            storeTlb(libraries, value.libraries)
            storeTlb(maybeBlkMasterInfo, value.master_ref)
        }
        storeTlb(maybeMcStateExtra, value.custom)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): ShardStateUnsplit = cellSlice {
        val globalId = loadUInt32()
        val shardId = loadTlb(ShardIdent)
        val seqNo = loadUInt32()
        val verSeqNo = loadUInt32()
        val genUtime = loadUInt32()
        val genLt = loadUInt64()
        val minRefMcSeqno = loadUInt32()
        val outMsgQueueInfo = loadRef {
            loadTlb(OutMsgQueueInfo)
        }
        val beforeSplit = loadBit()
        val accounts = loadRef {
            loadTlb(shardAccounts)
        }
        val custom = loadTlb(maybeMcStateExtra)
        loadRef {
            val overloadHistory = loadUInt64()
            val underloadHistory = loadUInt64()
            val totalBalance = loadTlb(CurrencyCollection)
            val totalValidatorFees = loadTlb(CurrencyCollection)
            val libraries = loadTlb(libraries)
            val masterRef = loadTlb(maybeBlkMasterInfo)
            ShardStateUnsplit(
                globalId,
                shardId,
                seqNo,
                verSeqNo,
                genUtime,
                genLt,
                minRefMcSeqno,
                outMsgQueueInfo,
                beforeSplit,
                accounts,
                overloadHistory,
                underloadHistory,
                totalBalance,
                totalValidatorFees,
                libraries,
                masterRef,
                custom
            )
        }
    }
}
