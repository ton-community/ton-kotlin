package org.ton.lite.api

import org.ton.lite.api.liteserver.functions.*
import org.ton.logger.Logger

interface LiteApi :
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

    LiteServerGetTransactionsFunction,

    LiteServerQueryFunction {

    val logger: Logger

    suspend fun sendRawQuery(byteArray: ByteArray): ByteArray

    override suspend fun query(liteServerQuery: LiteServerQuery): ByteArray {
        val liteServerQueryBytes = LiteServerQuery.encodeBoxed(liteServerQuery)
        return sendRawQuery(liteServerQueryBytes)
    }
}
