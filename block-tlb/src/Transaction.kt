package org.ton.block

import kotlinx.serialization.SerialName
import org.ton.bitstring.BitString
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.hashmap.HashMapE
import org.ton.tlb.*
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbCombinatorProvider
import org.ton.tlb.providers.TlbConstructorProvider
import kotlin.jvm.JvmName


@SerialName("transaction")
public data class Transaction(
    @SerialName("account_addr")
    @get:JvmName("accountAddr")
    val accountAddr: BitString, // account_addr : bits256

    @SerialName("lt")
    @get:JvmName("lt")
    val lt: ULong, // lt : uint64

    @SerialName("prev_trans_hash")
    @get:JvmName("prevTransHash")
    val prevTransHash: BitString, // prev_trans_hash : bits256

    @SerialName("prev_trans_lt")
    @get:JvmName("prevTransLt")
    val prevTransLt: ULong, // prev_trans_lt : uint64

    @SerialName("now")
    @get:JvmName("now")
    val now: UInt, // now : uint32

    @SerialName("outmsg_cnt")
    @get:JvmName("outMsgCnt")
    val outMsgCnt: Int, // outmsg_cnt : uint15

    @SerialName("orig_status")
    @get:JvmName("origStatus")
    val origStatus: AccountStatus, // orig_status : AccountStatus

    @SerialName("end_status")
    @get:JvmName("endStatus")
    val endStatus: AccountStatus, // end_status : AccountStatus

    @get:JvmName("r1")
    val r1: CellRef<TransactionAux>, // r1 : Aux

    @SerialName("total_fees")
    @get:JvmName("totalFees")
    val totalFees: CurrencyCollection, // total_fees : CurrencyCollection

    @SerialName("state_update")
    @get:JvmName("stateUpdate")
    val stateUpdate: CellRef<HashUpdate>, // state_update : ^HashUpdate

    @SerialName("description")
    @get:JvmName("description")
    val description: CellRef<TransactionDescr> // description : ^TransactionDescr
) : TlbObject {
    init {
        require(accountAddr.size == 256) { "expected accountAddr.size == 256, actual: ${accountAddr.size}" }
        require(prevTransHash.size == 256) { "expected prevTransHash.size == 256, actual: ${accountAddr.size}" }
    }

    public fun toCell(): Cell = CellBuilder.createCell {
        storeTlb(Transaction, this@Transaction)
    }

    public fun hash(): BitString = toCell().hash()

    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer {
        type("transaction") {
            field("account_addr", accountAddr)
            field("lt", lt)
            field("prev_trans_hash", prevTransHash)
            field("prev_trans_lt", prevTransLt)
            field("now", now)
            field("outmsg_cnt", outMsgCnt)
            field("orig_status", origStatus)
            field("end_status", endStatus)
            field(r1)
            field("total_fees", totalFees)
            field("state_update", stateUpdate)
            field("description", description)
        }
    }

    override fun toString(): String = print().toString()

    public companion object : TlbCombinatorProvider<Transaction> by TransactionTlConstructor.asTlbCombinator()
}


public data class TransactionAux(
    @SerialName("in_msg")
    @get:JvmName("inMsg")
    val inMsg: Maybe<CellRef<Message<Cell>>>,

    @SerialName("out_msgs")
    @get:JvmName("outMsgs")
    val outMsgs: HashMapE<CellRef<Message<Cell>>>,
) : TlbObject {
    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter {
        return printer.type {
            field("in_msg", inMsg)
            field("out_msgs", outMsgs)
        }
    }

    override fun toString(): String = print().toString()

    public companion object : TlbConstructorProvider<TransactionAux> by TransactionAuxTlbConstructor
}

private object TransactionTlConstructor : TlbConstructor<Transaction>(
    schema = "transaction\$0111 " +
            "  account_addr:bits256 " +
            "  lt:uint64 " +
            "  prev_trans_hash:bits256 " +
            "  prev_trans_lt:uint64 " +
            "  now:uint32 " +
            "  outmsg_cnt:uint15 " +
            "  orig_status:AccountStatus " +
            "  end_status:AccountStatus " +
            "  ^[ in_msg:(Maybe ^(Message Any)) out_msgs:(HashmapE 15 ^(Message Any)) ] " +
            "  total_fees:CurrencyCollection state_update:^(HASH_UPDATE Account) " +
            "  description:^TransactionDescr = Transaction;"
) {
    override fun loadTlb(cellSlice: CellSlice): Transaction = cellSlice {
        val accountAddr = loadBits(256)
        val lt = loadUInt64()
        val prevTransHash = loadBits(256)
        val prevTransLt = loadUInt64()
        val now = loadUInt32()
        val outmsgCnt = loadUInt(15).toInt()
        val origStatus = loadTlb(AccountStatus)
        val endStatus = loadTlb(AccountStatus)
        val r1 = loadRef(TransactionAux)
        val totalFees = loadTlb(CurrencyCollection)
        val stateUpdate = loadRef(HashUpdate)
        val description = loadRef(TransactionDescr)
        Transaction(
            accountAddr,
            lt,
            prevTransHash,
            prevTransLt,
            now,
            outmsgCnt,
            origStatus,
            endStatus,
            r1,
            totalFees,
            stateUpdate,
            description
        )
    }

    override fun storeTlb(cellBuilder: CellBuilder, value: Transaction) = cellBuilder {
        storeBits(value.accountAddr)
        storeUInt64(value.lt)
        storeBits(value.prevTransHash)
        storeUInt64(value.prevTransLt)
        storeUInt32(value.now)
        storeUInt(value.outMsgCnt, 15)
        storeTlb(AccountStatus, value.origStatus)
        storeTlb(AccountStatus, value.endStatus)
        storeRef(TransactionAux, value.r1)
        storeTlb(CurrencyCollection, value.totalFees)
        storeRef(HashUpdate, value.stateUpdate)
        storeRef(TransactionDescr, value.description)
    }
}

private object TransactionAuxTlbConstructor : TlbConstructor<TransactionAux>(
    schema = "\$_ in_msg:(Maybe ^(Message Any)) out_msgs:(HashmapE 15 ^(Message Any)) "
) {
    val maybeMessage = Maybe.tlbCodec(CellRef.tlbCodec(Message.Any))
    val outMsgs = HashMapE.tlbCodec(15, CellRef.tlbCodec(Message.Any))

    override fun storeTlb(cellBuilder: CellBuilder, value: TransactionAux) = cellBuilder {
        storeTlb(maybeMessage, value.inMsg)
        storeTlb(outMsgs, value.outMsgs)
    }

    override fun loadTlb(cellSlice: CellSlice): TransactionAux = cellSlice {
        TransactionAux(
            inMsg = loadTlb(maybeMessage),
            outMsgs = loadTlb(outMsgs)
        )
    }
}
