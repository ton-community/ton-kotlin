package org.ton.lite.api

import org.ton.lite.api.liteserver.LiteServerLookupBlockFunction
import org.ton.lite.api.liteserver.functions.*

fun interface LiteApi :
    LiteServerGetMasterchainInfoFunction,
    LiteServerGetMasterchainInfoExtFunction,
    LiteServerGetTimeFunction,
    LiteServerGetVersionFunction,
    LiteServerGetBlockFunction,
    LiteServerGetStateFunction,
    LiteServerGetBlockHeaderFunction,
    LiteServerSendMessageFunction,
    LiteServerGetAccountStateFunction,
    LiteServerRunSmcMethodFunction,
    LiteServerGetShardInfoFunction,
    LiteServerGetAllShardsInfoFunction,
    LiteServerGetOneTransactionFunction,
    LiteServerGetTransactionsFunction,
    LiteServerLookupBlockFunction,
    LiteServerListBlockTransactionsFunction,
    LiteServerGetBlockProofFunction,
    LiteServerGetConfigAllFunction,
    LiteServerGetConfigParamsFunction,
    LiteServerGetValidatorStatsFunction,
    LiteServerQueryFunction {

    suspend fun sendRawQuery(byteArray: ByteArray): ByteArray

    override suspend fun query(liteServerQuery: LiteServerQuery): ByteArray {
        val liteServerQueryBytes = LiteServerQuery.encodeBoxed(liteServerQuery)
        return sendRawQuery(liteServerQueryBytes)
    }

    suspend operator fun <T> invoke(liteApi: suspend LiteApi.() -> T): T = liteApi(this)
}
