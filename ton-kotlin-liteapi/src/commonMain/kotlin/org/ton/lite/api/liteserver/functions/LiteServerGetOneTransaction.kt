package org.ton.lite.api.liteserver.functions

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.lite.api.liteserver.LiteServerAccountId
import org.ton.lite.api.liteserver.LiteServerTransactionInfo
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readLongTl
import org.ton.tl.constructors.writeLongTl
import org.ton.tl.readTl
import org.ton.tl.writeTl

interface LiteServerGetOneTransactionFunction : LiteServerQueryFunction {
    suspend fun query(query: LiteServerGetOneTransaction) =
        query(query, LiteServerGetOneTransaction, LiteServerTransactionInfo)

    suspend fun getOneTransaction(id: TonNodeBlockIdExt, account: LiteServerAccountId, lt: Long) =
        query(LiteServerGetOneTransaction(id, account, lt))
}

@Serializable
data class LiteServerGetOneTransaction(
    val id: TonNodeBlockIdExt,
    val account: LiteServerAccountId,
    val lt: Long
) {
    companion object : TlCodec<LiteServerGetOneTransaction> by LiteServerGetOneTransactionTlConstructor
}

private object LiteServerGetOneTransactionTlConstructor : TlConstructor<LiteServerGetOneTransaction>(
    schema = "liteServer.getOneTransaction id:tonNode.blockIdExt account:liteServer.accountId lt:long = liteServer.TransactionInfo"
) {
    override fun decode(input: Input): LiteServerGetOneTransaction {
        val id = input.readTl(TonNodeBlockIdExt)
        val account = input.readTl(LiteServerAccountId)
        val lt = input.readLongTl()
        return LiteServerGetOneTransaction(id, account, lt)
    }

    override fun encode(output: Output, value: LiteServerGetOneTransaction) {
        output.writeTl(TonNodeBlockIdExt, value.id)
        output.writeTl(LiteServerAccountId, value.account)
        output.writeLongTl(value.lt)
    }
}
