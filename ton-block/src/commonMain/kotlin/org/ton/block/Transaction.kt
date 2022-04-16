@file:UseSerializers(HexByteArraySerializer::class)

package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.ton.crypto.HexByteArraySerializer
import org.ton.crypto.hex
import org.ton.hashmap.HashMapE

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
    val state_update: HashUpdate<org.ton.block.Account>,
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

    override fun toString(): String = buildString {
        append("Transaction(account_addr=")
        append(hex(account_addr))
        append(", lt=")
        append(lt)
        append(", prev_trans_hash=")
        append(hex(prev_trans_hash))
        append(", prev_trans_lt=")
        append(prev_trans_lt)
        append(", now=")
        append(now)
        append(", outmsg_cnt=")
        append(outmsg_cnt)
        append(", orig_status=")
        append(orig_status)
        append(", end_status=")
        append(end_status)
        append(", in_msg=")
        append(in_msg)
        append(", out_msgs=")
        append(out_msgs)
        append(", total_fees=")
        append(total_fees)
        append(", state_update=")
        append(state_update)
        append(", description=")
        append(description)
        append(")")
    }
}