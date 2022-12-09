package org.ton.lite.api.liteserver.functions

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.block.AddrStd
import org.ton.lite.api.liteserver.LiteServerAccountId
import org.ton.lite.api.liteserver.LiteServerAccountState
import org.ton.tl.TlConstructor
import org.ton.tl.readTl
import org.ton.tl.writeTl

fun interface LiteServerGetAccountStateFunction : LiteServerQueryFunction {
    suspend fun query(query: LiteServerGetAccountState) =
        query(query, LiteServerGetAccountState, LiteServerAccountState)

    suspend fun getAccountState(id: TonNodeBlockIdExt, account: LiteServerAccountId) =
        query(LiteServerGetAccountState(id, account))

    suspend fun getAccountState(
        id: TonNodeBlockIdExt, account: AddrStd
    ) = query(LiteServerGetAccountState(id, LiteServerAccountId(account)))
}

@Serializable
data class LiteServerGetAccountState(
    val id: TonNodeBlockIdExt,
    val account: LiteServerAccountId,
) {
    companion object : TlConstructor<LiteServerGetAccountState>(
        schema = "liteServer.getAccountState id:tonNode.blockIdExt account:liteServer.accountId = liteServer.AccountState"
    ) {
        override fun decode(input: Input): LiteServerGetAccountState {
            val id = input.readTl(TonNodeBlockIdExt)
            val account = input.readTl(LiteServerAccountId)
            return LiteServerGetAccountState(id, account)
        }

        override fun encode(output: Output, value: LiteServerGetAccountState) {
            output.writeTl(TonNodeBlockIdExt, value.id)
            output.writeTl(LiteServerAccountId, value.account)
        }
    }
}
