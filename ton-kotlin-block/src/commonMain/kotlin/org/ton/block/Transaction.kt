@file:UseSerializers(HexByteArraySerializer::class)

package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.ton.bitstring.BitString
import org.ton.cell.*
import org.ton.crypto.HexByteArraySerializer
import org.ton.hashmap.HashMapE
import org.ton.tlb.*
import org.ton.tlb.constructor.tlbCodec

@SerialName("transaction")
@Serializable
data class Transaction(
    val account_addr: BitString,
    val lt: ULong,
    val prev_trans_hash: BitString,
    val prev_trans_lt: ULong,
    val now: UInt,
    val outmsg_cnt: Int,
    val orig_status: AccountStatus,
    val end_status: AccountStatus,
    val in_msg: Maybe<Message<Cell>>,
    val out_msgs: HashMapE<Message<Cell>>,
    val total_fees: CurrencyCollection,
    val state_update: HashUpdate,
    val description: TransactionDescr
) {
    init {
        require(account_addr.size == 256) { "required: account_addr.size == 256, actual: ${account_addr.size}" }
        require(prev_trans_hash.size == 256) { "required: prev_trans_hash.size == 256, actual: ${prev_trans_hash.size}" }
    }

    fun toCell(): Cell = CellBuilder.createCell {
        storeTlb(Transaction, this@Transaction)
    }

    fun hash(): ByteArray = toCell().hash()

    companion object : TlbCodec<Transaction> by TransactionTlbConstructor.asTlbCombinator()

    override fun toString(): String = buildString {
        append("(transaction\n")
        append("account_addr:")
        append(account_addr)
        append(" lt:")
        append(lt)
        append(" prev_trans_hash:")
        append(prev_trans_hash)
        append(" prev_trans_lt:")
        append(prev_trans_lt)
        append(" now:")
        append(now)
        append(" outmsg_cnt:")
        append(outmsg_cnt)
        append(" orig_status:")
        append(orig_status)
        append(" end_status:")
        append(end_status)
        append(" in_msg:")
        append(in_msg)
        append(" out_msgs:")
        append(out_msgs)
        append(" total_fees:")
        append(total_fees)
        append(" state_update:")
        append(state_update)
        append(" description:")
        append(description)
        append(")")
    }
}

private object TransactionTlbConstructor : TlbConstructor<Transaction>(
    schema = "transaction\$0111 account_addr:bits256 lt:uint64 " +
        "prev_trans_hash:bits256 prev_trans_lt:uint64 now:uint32 " +
        "outmsg_cnt:uint15 " +
        "orig_status:AccountStatus end_status:AccountStatus " +
        "^[ in_msg:(Maybe ^(Message Any)) out_msgs:(HashmapE 15 ^(Message Any)) ] " +
        "total_fees:CurrencyCollection state_update:^(HASH_UPDATE Account) " +
        "description:^TransactionDescr = Transaction;"
) {
    val RefMessageAny = Cell.tlbCodec(Message.Any)
    val maybeMessageAny = Maybe(RefMessageAny)
    val hashMapEMessageAny = HashMapE.tlbCodec(15, RefMessageAny)

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: Transaction
    ) = cellBuilder {
        storeBits(value.account_addr)
        storeUInt64(value.lt)
        storeBits(value.prev_trans_hash)
        storeUInt64(value.prev_trans_lt)
        storeUInt32(value.now)
        storeUInt(value.outmsg_cnt, 15)
        storeTlb(AccountStatus, value.orig_status)
        storeTlb(AccountStatus, value.end_status)
        storeRef {
            storeTlb(maybeMessageAny, value.in_msg)
            storeTlb(hashMapEMessageAny, value.out_msgs)
        }
        storeTlb(CurrencyCollection, value.total_fees)
        storeRef {
            storeTlb(HashUpdate, value.state_update)
        }
        storeRef {
            storeTlb(TransactionDescr, value.description)
        }
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): Transaction = cellSlice {
        val accountAddr = loadBits(256)
        val lt = loadUInt64()
        val prevTransHash = loadBits(256)
        val prevTransLt = loadUInt64()
        val now = loadUInt32()
        val outmsgCnt = loadUInt(15).toInt()
        val origStatus = loadTlb(AccountStatus)
        val endStatus = loadTlb(AccountStatus)
        val (inMsg, outMsgs) = loadRef {
            val inMsg = loadTlb(maybeMessageAny)
            val outMsgs = loadTlb(hashMapEMessageAny)
            inMsg to outMsgs
        }
        val totalFees = loadTlb(CurrencyCollection)
        val stateUpdate = loadRef {
            loadTlb(HashUpdate)
        }
        val description = loadRef {
            loadTlb(TransactionDescr)
        }
        Transaction(
            accountAddr,
            lt,
            prevTransHash,
            prevTransLt,
            now,
            outmsgCnt,
            origStatus,
            endStatus,
            inMsg,
            outMsgs,
            totalFees,
            stateUpdate,
            description
        )
    }
}
