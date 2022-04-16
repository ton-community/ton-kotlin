@file:UseSerializers(HexByteArraySerializer::class)

package ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import ton.crypto.HexByteArraySerializer
import ton.crypto.hex
import ton.hashmap.HashMapE

@SerialName("transaction")
@Serializable
data class Transaction(
    val account_addr: ByteArray,
    val lt: Long,
    val prev_trans_hash: ByteArray,
    val prev_trans_lt: Long,
    val now: Long,
    val outmsg_cnt: Int,
    val orig_status: AccountStatus,
    val end_status: AccountStatus,
    val in_msg: Message<ByteArray>?,
    val out_msgs: HashMapE<Message<ByteArray>>,
    val total_fees: CurrencyCollection,
    val state_update: HashUpdate<Account>,
    val description: TransactionDescr
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Transaction

        if (!account_addr.contentEquals(other.account_addr)) return false
        if (lt != other.lt) return false
        if (!prev_trans_hash.contentEquals(other.prev_trans_hash)) return false
        if (prev_trans_lt != other.prev_trans_lt) return false
        if (now != other.now) return false
        if (outmsg_cnt != other.outmsg_cnt) return false
        if (orig_status != other.orig_status) return false
        if (end_status != other.end_status) return false
        if (in_msg != other.in_msg) return false
        if (out_msgs != other.out_msgs) return false
        if (total_fees != other.total_fees) return false
        if (state_update != other.state_update) return false
        if (description != other.description) return false

        return true
    }

    override fun hashCode(): Int {
        var result = account_addr.contentHashCode()
        result = 31 * result + lt.hashCode()
        result = 31 * result + prev_trans_hash.contentHashCode()
        result = 31 * result + prev_trans_lt.hashCode()
        result = 31 * result + now.hashCode()
        result = 31 * result + outmsg_cnt
        result = 31 * result + orig_status.hashCode()
        result = 31 * result + end_status.hashCode()
        result = 31 * result + in_msg.hashCode()
        result = 31 * result + out_msgs.hashCode()
        result = 31 * result + total_fees.hashCode()
        result = 31 * result + state_update.hashCode()
        result = 31 * result + description.hashCode()
        return result
    }

    override fun toString(): String =
        "Transaction(account_addr=${hex(account_addr)}, lt=$lt, prev_trans_hash=${hex(prev_trans_hash)}, prev_trans_lt=$prev_trans_lt, now=$now, outmsg_cnt=$outmsg_cnt, orig_status=$orig_status, end_status=$end_status, in_msg=$in_msg, out_msgs=$out_msgs, total_fees=$total_fees, state_update=$state_update, description=$description)"
}