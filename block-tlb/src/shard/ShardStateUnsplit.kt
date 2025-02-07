package org.ton.kotlin.shard

import kotlinx.io.bytestring.ByteString
import org.ton.kotlin.bitstring.toBitString
import org.ton.kotlin.block.BlockRef
import org.ton.kotlin.cell.CellBuilder
import org.ton.kotlin.cell.CellContext
import org.ton.kotlin.cell.CellRef
import org.ton.kotlin.cell.CellSlice
import org.ton.kotlin.cell.serialization.CellSerializer
import org.ton.kotlin.currency.CurrencyCollection
import org.ton.kotlin.dict.Dictionary
import org.ton.kotlin.dict.RawDictionary

/**
 * State of the single shard.
 */
public data class ShardStateUnsplit(
    /**
     * Global network id.
     */
    val globalId: Int,

    /**
     * Id of the shard.
     */
    val shardId: ShardIdent,

    /**
     * Sequence number of the corresponding block.
     */
    val seqno: Int,

    /**
     * Vertical sequence number of the corresponding block.
     */
    val vertSeqno: Int,

    /**
     * Unix timestamp in seconds when the block was created.
     */
    val genTime: Long,

    /**
     * Logical time when the state was created.
     */
    val genLt: Long,

    /**
     * Minimal referenced seqno of the masterchain block.
     */
    val minRefMcSeqno: Int,

    /**
     * Output messages queue info.
     *
     * TODO: implement outMsgQueueInfo
     */
    val outMsgQueueInfo: CellRef<CellSlice>,

    /**
     * Whether this state was produced before the shards split.
     */
    val beforeSplit: Boolean,

    /**
     * Reference to the dictionary with shard accounts.
     */
    val accounts: CellRef<ShardAccounts>,

    /**
     * Mask for the overloaded blocks.
     */
    val overloadHistory: ULong,

    /**
     * Mask for the underloaded blocks.
     */
    val underloadHistory: ULong,

    /**
     * Total balance for all currencies.
     */
    val totalBalance: CurrencyCollection,

    /**
     * Total pending validator fees.
     */
    val totalValidatorFees: CurrencyCollection,

    /**
     * Dictionary with all libraries and its providers.
     */
    val libraries: Libraries,

    /**
     * Optional reference to the masterchain block.
     */
    val masterRef: BlockRef?,

    /**
     * Shard state additional masterchain data.
     */
    val custom: CellRef<McStateExtra>?
) : ShardState {
    /**
     * Tries to load output messages queue info.
     */
    public fun loadOutMsgQueueInfo(context: CellContext = CellContext.EMPTY): Result<CellSlice> =
        runCatching { outMsgQueueInfo.load(context) }

    /**
     * Tries to load shard accounts dictionary.
     */
    public fun loadAccounts(context: CellContext = CellContext.EMPTY): Result<ShardAccounts> =
        runCatching { accounts.load(context) }

    /**
     * Tries to load additional masterchain data.
     */
    public fun loadCustom(context: CellContext = CellContext.EMPTY): Result<McStateExtra?> =
        runCatching { custom?.load(context) }

    public class Libraries(
        dict: RawDictionary
    ) : Dictionary<ByteString, LibDescr>(
        dict = dict,
        keySerializer = { it.toBitString() },
        keyDeserializer = { ByteString(*it.toByteArray()) },
        valueSerializer = LibDescr.cellSerializer()
    )

    public companion object : CellSerializer<ShardStateUnsplit> by ShardStateUnsplitSerializer
}

/*


    public object Tlb : TlbConstructor<ShardStateUnsplit>(
        schema = "shard_state#9023afe2 " +
                "global_id:int32 " +
                "shard_id:ShardIdent " +
                "seq_no:uint32 " +
                "vert_seq_no:# " +
                "gen_utime:uint32 " +
                "gen_lt:uint64 " +
                "min_ref_mc_seqno:uint32 " +
                "out_msg_queue_info:^OutMsgQueueInfo " +
                "before_split:(## 1) " +
                "accounts:^ShardAccounts " +
                "^[ overload_history:uint64 " +
                "underload_history:uint64 " +
                "total_balance:CurrencyCollection " +
                "total_validator_fees:CurrencyCollection " +
                "libraries:(HashmapE 256 LibDescr) " +
                "master_ref:(Maybe BlkMasterInfo) ] " +
                "custom:(Maybe ^McStateExtra) " +
                "= ShardStateUnsplit;"
    ) {
        private val mcExtra = CellRef.tlbCodec(McStateExtra.Tlb)
        private val hashMapELibDesc = HashMapE.tlbCodec(256, LibDescr.Companion)
        private val shardAccounts = HashmapAugE.tlbCodec(256, ShardAccount.Tlb, DepthBalanceInfo.Tlb)

        override fun storeTlb(
            cellBuilder: CellBuilder,
            value: ShardStateUnsplit
        ): Unit = cellBuilder {
            storeInt(value.globalId, 32)
            storeTlb(ShardIdent.Companion, value.shardId)
            storeUInt(value.seqno.toInt(), 32)
            storeInt(value.vertSeqno, 32)
            storeUInt(value.genTime.toInt(), 32)
            storeUInt(value.genLt.toLong(), 64)
            storeUInt(value.minRefMcSeqno.toInt(), 32)
            storeRef(OutMsgQueueInfo, value.outMsgQueueInfo)
            storeBoolean(value.beforeSplit)
            storeRef(shardAccounts, value.accounts)
            storeRef {
                storeUInt(value.overloadHistory.toLong(), 64)
                storeUInt(value.underloadHistory.toLong(), 64)
                storeTlb(CurrencyCollection.Tlb, value.totalBalance)
                storeTlb(CurrencyCollection.Tlb, value.totalValidatorFees)
                storeTlb(hashMapELibDesc, value.libraries)
                storeNullableTlb(BlockRef.Tlb, value.masterRef)
            }
            storeNullableTlb(mcExtra, value.custom)
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): ShardStateUnsplit = cellSlice {
            val globalId = loadInt(32)
            val shardId = loadTlb(ShardIdent.Companion)
            val seqNo = loadUInt(32).toInt()
            val vertSeqNo = loadUInt(32).toInt()
            val genUtime = loadUInt(32).toLong()
            val genLt = loadULong(64).toLong()
            val minRefMcSeqno = loadUInt(32).toInt()
            val outMsgQueueInfo = loadRef(OutMsgQueueInfo)
            val beforeSplit = loadBit()
            val accounts = loadRef(shardAccounts)
            val r1 = loadRef().beginParse()
            val overloadHistory = r1.loadULong(64)
            val underloadHistory = r1.loadULong(64)
            val totalBalance = r1.loadTlb(CurrencyCollection.Tlb)
            val totalValidatorFees = r1.loadTlb(CurrencyCollection.Tlb)
            val libraries = r1.loadTlb(hashMapELibDesc)
            val masterRef = r1.loadNullableTlb(BlockRef.Tlb)
            val custom = loadNullableTlb(mcExtra)

            ShardStateUnsplit(
                globalId,
                shardId,
                seqNo,
                vertSeqNo,
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
 */
internal object ShardStateUnsplitSerializer : CellSerializer<ShardStateUnsplit> {
    override fun load(
        slice: CellSlice,
        context: CellContext
    ): ShardStateUnsplit {
        TODO("Not yet implemented")
    }

    override fun store(
        builder: CellBuilder,
        value: ShardStateUnsplit,
        context: CellContext
    ) {
        TODO("Not yet implemented")
    }

}