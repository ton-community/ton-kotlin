@file:UseSerializers(HexByteArraySerializer::class)

package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.ton.bitstring.BitString
import org.ton.cell.*
import org.ton.crypto.HexByteArraySerializer
import org.ton.hashmap.HashMapE
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.constructor.tlbCodec
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@SerialName("transaction")
@Serializable
data class Transaction(
    val account_addr: BitString,
    val lt: Long,
    val prev_trans_hash: BitString,
    val prev_trans_lt: Long,
    val now: Long,
    val outmsg_cnt: Int,
    val orig_status: AccountStatus,
    val end_status: AccountStatus,
    val in_msg: Maybe<Message<Cell>>,
    val out_msgs: HashMapE<Message<Cell>>,
    val total_fees: CurrencyCollection,
    val state_update: HashUpdate<Account>,
    val description: TransactionDescr
) {
    init {
        require(account_addr.size == 256) { "required: account_addr.size == 256, actual: ${account_addr.size}" }
        require(prev_trans_hash.size == 256) { "required: prev_trans_hash.size == 256, actual: ${prev_trans_hash.size}" }
    }

    companion object : TlbCodec<Transaction> by TransactionTlbConstructor.asTlbCombinator()
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
    val hashUpdateAccount = HashUpdate.tlbCodec(Account.tlbCodec())

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: Transaction
    ) = cellBuilder {
        storeBits(value.account_addr)
        storeUInt(value.lt, 64)
        storeBits(value.prev_trans_hash)
        storeUInt(value.prev_trans_lt, 64)
        storeUInt(value.now, 32)
        storeUInt(value.outmsg_cnt, 15)
        storeTlb(AccountStatus, value.orig_status)
        storeTlb(AccountStatus, value.end_status)
        storeRef {
            storeTlb(maybeMessageAny, value.in_msg)
            storeTlb(hashMapEMessageAny, value.out_msgs)
        }
        storeTlb(CurrencyCollection, value.total_fees)
        storeRef {
            storeTlb(hashUpdateAccount, value.state_update)
        }
        storeRef {
            storeTlb(TransactionDescr, value.description)
        }
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): Transaction = cellSlice {
        val accountAddr = loadBitString(256)
        val lt = loadUInt(64).toLong()
        val prevTransHash = loadBitString(256)
        val prevTransLt = loadUInt(64).toLong()
        val now = loadUInt(32).toLong()
        val outmsgCnt = loadUInt(15).toInt()
        val origStatus = loadTlb(AccountStatus)
        val endStatus = loadTlb(AccountStatus)
        val (inMsg, outMsgs) = loadRef {
            loadTlb(maybeMessageAny) to loadTlb(hashMapEMessageAny)
        }
        val totalFees = loadTlb(CurrencyCollection)
        val stateUpdate = loadRef {
            loadTlb(hashUpdateAccount)
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