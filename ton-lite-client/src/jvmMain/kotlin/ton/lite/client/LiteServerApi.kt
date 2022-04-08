package ton.lite.client

import io.ktor.utils.io.core.*
import ton.adnl.AdnlClient
import ton.adnl.TLCodec

interface LiteServerApi {
    val adnlClient: AdnlClient

    suspend fun <Q, A> sendQuery(query: Q, queryCodec: TLCodec<Q>, answerCodec: TLCodec<A>): A {
        val queryBytes = queryCodec.encodeBoxed(query)
        val liteServerQuery = LiteServerQuery(queryBytes)
        val liteServerQueryBytes = LiteServerQuery.encodeBoxed(liteServerQuery)
        val answerBytes = adnlClient.sendQuery(liteServerQueryBytes)
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