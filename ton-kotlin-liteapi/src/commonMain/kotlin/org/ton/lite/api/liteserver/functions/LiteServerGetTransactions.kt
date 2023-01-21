@file:UseSerializers(HexByteArraySerializer::class)

package org.ton.lite.api.liteserver.functions

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.ton.crypto.HexByteArraySerializer
import org.ton.lite.api.liteserver.LiteServerAccountId
import org.ton.lite.api.liteserver.LiteServerTransactionList
import org.ton.tl.*
import org.ton.tl.constructors.*

@Serializable
@SerialName("liteServer.getTransactions")
public data class LiteServerGetTransactions(
    val count: Int,
    val account: LiteServerAccountId,
    val lt: Long,
    val hash: ByteArray
) : TLFunction<LiteServerGetTransactions, LiteServerTransactionList> {
    init {
        require(hash.size == 32) { "hash must be 32 bytes long" }
    }

    override fun tlCodec(): TlCodec<LiteServerGetTransactions> = LiteServerGetTransactionsTlConstructor

    override fun resultTlCodec(): TlCodec<LiteServerTransactionList> = LiteServerTransactionList

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LiteServerGetTransactions) return false

        if (count != other.count) return false
        if (account != other.account) return false
        if (lt != other.lt) return false
        if (!hash.contentEquals(other.hash)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = count
        result = 31 * result + account.hashCode()
        result = 31 * result + lt.hashCode()
        result = 31 * result + hash.contentHashCode()
        return result
    }

    companion object : TlCodec<LiteServerGetTransactions> by LiteServerGetTransactionsTlConstructor
}

public object LiteServerGetTransactionsTlConstructor : TlConstructor<LiteServerGetTransactions>(
    schema = "liteServer.getTransactions count:# account:liteServer.accountId lt:long hash:int256 = liteServer.TransactionList"
) {
    override fun decode(reader: TlReader): LiteServerGetTransactions {
        val count = reader.readInt()
        val account = reader.read(LiteServerAccountId)
        val lt = reader.readLong()
        val hash = reader.readRaw(32)
        return LiteServerGetTransactions(count, account, lt, hash)
    }

    override fun encode(writer: TlWriter, value: LiteServerGetTransactions) {
        writer.writeInt(value.count)
        writer.write(LiteServerAccountId, value.account)
        writer.writeLong(value.lt)
        writer.writeRaw(value.hash)
    }
}
