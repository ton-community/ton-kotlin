@file:UseSerializers(HexByteArraySerializer::class)

package org.ton.lite.api.liteserver.functions

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.ton.crypto.HexByteArraySerializer
import org.ton.crypto.encodeHex
import org.ton.lite.api.liteserver.LiteServerAccountId
import org.ton.lite.api.liteserver.LiteServerTransactionList
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.*
import org.ton.tl.readTl
import org.ton.tl.writeTl

interface LiteServerGetTransactionsFunction : LiteServerQueryFunction {
    suspend fun query(query: LiteServerGetTransactions) =
        query(query, LiteServerGetTransactions, LiteServerTransactionList)

    suspend fun getTransactions(count: Int, account: LiteServerAccountId, lt: Long, hash: ByteArray) =
        query(LiteServerGetTransactions(count, account, lt, hash))
}

@Serializable
data class LiteServerGetTransactions(
    val count: Int,
    val account: LiteServerAccountId,
    val lt: Long,
    val hash: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LiteServerGetTransactions

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

    override fun toString() = buildString {
        append("LiteServerGetTransactions(count=")
        append(count)
        append(", account=")
        append(account)
        append(", lt=")
        append(lt)
        append(", hash=")
        append(hash.encodeHex())
        append(")")
    }

    companion object : TlConstructor<LiteServerGetTransactions>(
        type = LiteServerGetTransactions::class,
        schema = "liteServer.getTransactions count:# account:liteServer.accountId lt:long hash:int256 = liteServer.TransactionList"
    ) {
        override fun decode(input: Input): LiteServerGetTransactions {
            val count = input.readIntTl()
            val account = input.readTl(LiteServerAccountId)
            val lt = input.readLongTl()
            val hash = input.readInt256Tl()
            return LiteServerGetTransactions(count, account, lt, hash)
        }

        override fun encode(output: Output, value: LiteServerGetTransactions) {
            output.writeIntTl(value.count)
            output.writeTl(LiteServerAccountId, value.account)
            output.writeLongTl(value.lt)
            output.writeInt256Tl(value.hash)
        }
    }
}
