package org.ton.lite.api.liteserver

import kotlinx.serialization.Serializable
import org.ton.tl.*

@Serializable
public data class LiteServerTransactionId3(
    val account: Bits256,
    val lt: Long
) {
    public companion object : TlCodec<LiteServerTransactionId3> by LiteServerTransactionId3TlConstructor
}

private object LiteServerTransactionId3TlConstructor : TlConstructor<LiteServerTransactionId3>(
    schema = "liteServer.transactionId3 account:int256 lt:long = liteServer.TransactionId3"
) {
    override fun decode(reader: TlReader): LiteServerTransactionId3 {
        val account = reader.readBits256()
        val lt = reader.readLong()
        return LiteServerTransactionId3(account, lt)
    }

    override fun encode(writer: TlWriter, value: LiteServerTransactionId3) {
        writer.writeBits256(value.account)
        writer.writeLong(value.lt)
    }
}
