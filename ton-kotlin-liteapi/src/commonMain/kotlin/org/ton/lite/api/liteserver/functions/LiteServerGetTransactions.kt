@file:UseSerializers(HexByteArraySerializer::class)

package org.ton.lite.api.liteserver.functions

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.ton.crypto.HexByteArraySerializer
import org.ton.lite.api.liteserver.LiteServerAccountId
import org.ton.lite.api.liteserver.LiteServerTransactionList
import org.ton.tl.*
import org.ton.tl.ByteString.Companion.toByteString

@Serializable
@SerialName("liteServer.getTransactions")
public data class LiteServerGetTransactions(
    val count: Int,
    val account: LiteServerAccountId,
    val lt: Long,
    val hash: ByteString
) : TLFunction<LiteServerGetTransactions, LiteServerTransactionList> {
    public constructor(count: Int, account: LiteServerAccountId, lt: Long, hash: ByteArray) : this(
        count, account, lt, hash.toByteString()
    )

    init {
        require(hash.size == 32)
    }

    override fun tlCodec(): TlCodec<LiteServerGetTransactions> = LiteServerGetTransactionsTlConstructor

    override fun resultTlCodec(): TlCodec<LiteServerTransactionList> = LiteServerTransactionList

    public companion object : TlCodec<LiteServerGetTransactions> by LiteServerGetTransactionsTlConstructor
}

public object LiteServerGetTransactionsTlConstructor : TlConstructor<LiteServerGetTransactions>(
    schema = "liteServer.getTransactions count:# account:liteServer.accountId lt:long hash:int256 = liteServer.TransactionList"
) {
    override fun decode(reader: TlReader): LiteServerGetTransactions {
        val count = reader.readInt()
        val account = reader.read(LiteServerAccountId)
        val lt = reader.readLong()
        val hash = reader.readByteString(32)
        return LiteServerGetTransactions(count, account, lt, hash)
    }

    override fun encode(writer: TlWriter, value: LiteServerGetTransactions) {
        writer.writeInt(value.count)
        writer.write(LiteServerAccountId, value.account)
        writer.writeLong(value.lt)
        writer.writeRaw(value.hash)
    }
}
