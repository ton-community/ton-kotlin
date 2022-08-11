package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.*
import org.ton.hashmap.AugDictionary
import org.ton.hashmap.HashMapE
import org.ton.tlb.TlbConstructor
import org.ton.tlb.constructor.tlbCodec
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@Serializable
@SerialName("shard_state")
data class ShardStateUnsplit(
    val global_id: Long,
    val shard_id: ShardIdent,
    val seq_no: Long,
    val vert_seq_no: Int,
    val gen_utime: Long,
    val gen_lt: Long,
    val min_ref_mc_seqno: Long,
    val out_msg_queue_info: OutMsgQueueInfo,
    val before_split: Boolean,
    val accounts: AugDictionary<ShardAccount, DepthBalanceInfo>,
    val overload_history: Long,
    val underload_history: Long,
    val total_balance: CurrencyCollection,
    val total_validator_fees: CurrencyCollection,
    val libraries: HashMapE<LibDescr>,
    val master_ref: Maybe<BlkMasterInfo>,
    val custom: Maybe<McStateExtra>
) : ShardState {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<ShardStateUnsplit> = ShardStateUnsplitTlbConstructor
    }
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
    val outMsgQueueInfo by lazy { OutMsgQueueInfo.tlbCodec() }
    val shardAccounts by lazy { AugDictionary.tlbCodec(256, ShardAccount.tlbCodec(), DepthBalanceInfo.tlbCodec()) }
    val currencyCollection by lazy { CurrencyCollection.tlbCodec() }
    val libraries by lazy { HashMapE.tlbCodec(256, LibDescr.tlbCodec()) }
    val maybeBlkMasterInfo by lazy { Maybe.tlbCodec(BlkMasterInfo) }
    val maybeMcStateExtra by lazy { Maybe.tlbCodec(Cell.tlbCodec(McStateExtra)) }

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: ShardStateUnsplit
    ) = cellBuilder {
        storeUInt(value.global_id, 32)
        storeTlb(ShardIdent, value.shard_id)
        storeUInt(value.seq_no, 32)
        storeUInt(value.vert_seq_no, 32)
        storeUInt(value.min_ref_mc_seqno, 32)
        storeRef {
            storeTlb(outMsgQueueInfo, value.out_msg_queue_info)
        }
        storeBits(value.before_split)
        storeRef {
            storeTlb(shardAccounts, value.accounts)
        }
        storeRef {
            storeUInt(value.overload_history, 64)
            storeUInt(value.underload_history, 64)
            storeTlb(currencyCollection, value.total_balance)
            storeTlb(currencyCollection, value.total_validator_fees)
            storeTlb(libraries, value.libraries)
            storeTlb(maybeBlkMasterInfo, value.master_ref)
        }
        storeTlb(maybeMcStateExtra, value.custom)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): ShardStateUnsplit = cellSlice {
        val globalId = loadUInt(32).toLong()
        val shardId = loadTlb(ShardIdent)
        val seqNo = loadUInt(32).toLong()
        val verSeqNo = loadUInt(32).toInt()
        val genUtime = loadUInt(32).toLong()
        val genLt = loadUInt(64).toLong()
        val minRefMcSeqno = loadUInt(32).toLong()
        val outMsgQueueInfo = loadRef {
            loadTlb(outMsgQueueInfo)
        }
        val beforeSplit = loadBit()
        val accounts = loadRef {
            loadTlb(shardAccounts)
        }
        val custom = loadTlb(maybeMcStateExtra)
        loadRef {
            val overloadHistory = loadUInt(64).toLong()
            val underloadHistory = loadUInt(64).toLong()
            val totalBalance = loadTlb(currencyCollection)
            val totalValidatorFees = loadTlb(currencyCollection)
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
