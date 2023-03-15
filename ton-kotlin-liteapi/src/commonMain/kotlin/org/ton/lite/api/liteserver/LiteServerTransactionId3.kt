package org.ton.lite.api.liteserver

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.TlReader
import org.ton.tl.TlWriter

@Serializable
@SerialName("liteServer.transactionId3")
public data class LiteServerTransactionId3(
    val account: ByteArray,
    val lt: Long
) {
    init {
        require(account.size == 32) { "account must be 32 bytes long" }
    }

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

    public companion object : TlCodec<LiteServerTransactionId3> by LiteServerTransactionId3TlConstructor
}

private object LiteServerTransactionId3TlConstructor : TlConstructor<LiteServerTransactionId3>(
    schema = "liteServer.transactionId3 account:int256 lt:long = liteServer.TransactionId3"
) {
    override fun decode(reader: TlReader): LiteServerTransactionId3 {
        val account = reader.readRaw(32)
        val lt = reader.readLong()
        return LiteServerTransactionId3(account, lt)
    }

    override fun encode(writer: TlWriter, value: LiteServerTransactionId3) {
        writer.writeRaw(value.account)
        writer.writeLong(value.lt)
    }
}
