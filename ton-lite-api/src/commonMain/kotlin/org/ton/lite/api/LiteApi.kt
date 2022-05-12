package org.ton.lite.api

import io.ktor.utils.io.core.*
import org.ton.lite.api.liteserver.*
import org.ton.lite.api.tonnode.TonNodeBlockIdExt
import org.ton.tl.TlConstructor

interface LiteApi {
    suspend fun sendRawQuery(byteArray: ByteArray): ByteArray

    suspend fun <Q : Any, A : Any> sendQuery(query: Q, queryCodec: TlConstructor<Q>, answerCodec: TlConstructor<A>): A {
        val queryBytes = queryCodec.encodeBoxed(query)
        val liteServerQuery = LiteServerQuery(queryBytes)
        val liteServerQueryBytes = LiteServerQuery.encodeBoxed(liteServerQuery)
        val answerBytes = sendRawQuery(liteServerQueryBytes)
        val errorByteInput = ByteReadPacket(answerBytes)
        if (errorByteInput.readIntLittleEndian() == LiteServerError.id) {
            throw LiteServerError.decode(errorByteInput)
        }
        return answerCodec.decodeBoxed(answerBytes)
    }

    suspend fun getTime() =
            sendQuery(LiteServerGetTime, LiteServerGetTime, LiteServerCurrentTime)

    suspend fun getMasterchainInfo() =
            sendQuery(LiteServerGetMasterchainInfo, LiteServerGetMasterchainInfo, LiteServerMasterchainInfo)

    suspend fun getAccountState(id: TonNodeBlockIdExt, account: LiteServerAccountId) =
            getAccountState(LiteServerGetAccountState(id, account))

    suspend fun getAccountState(query: LiteServerGetAccountState) =
            sendQuery(query, LiteServerGetAccountState, LiteServerAccountState)
}