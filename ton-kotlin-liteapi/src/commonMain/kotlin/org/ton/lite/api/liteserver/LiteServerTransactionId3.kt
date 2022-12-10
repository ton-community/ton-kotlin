package org.ton.lite.api.liteserver

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readInt256Tl
import org.ton.tl.constructors.readLongTl
import org.ton.tl.constructors.writeInt256Tl
import org.ton.tl.constructors.writeLongTl

@Serializable
data class LiteServerTransactionId3(
    val account: ByteArray,
    val lt: Long
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LiteServerTransactionId3) return false
        if (!account.contentEquals(other.account)) return false
        if (lt != other.lt) return false
        return true
    }

    override fun hashCode(): Int {
        var result = account.contentHashCode()
        result = 31 * result + lt.hashCode()
        return result
    }

    companion object : TlCodec<LiteServerTransactionId3> by LiteServerTransactionId3TlConstructor
}

private object LiteServerTransactionId3TlConstructor : TlConstructor<LiteServerTransactionId3>(
    schema = "liteServer.transactionId3 account:int256 lt:long = liteServer.TransactionId3"
) {
    override fun decode(input: Input): LiteServerTransactionId3 {
        val account = input.readInt256Tl()
        val lt = input.readLongTl()
        return LiteServerTransactionId3(account, lt)
    }

    override fun encode(output: Output, value: LiteServerTransactionId3) {
        output.writeInt256Tl(value.account)
        output.writeLongTl(value.lt)
    }
}
