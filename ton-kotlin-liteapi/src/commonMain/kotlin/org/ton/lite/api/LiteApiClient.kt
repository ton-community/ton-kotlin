package org.ton.lite.api

import io.ktor.utils.io.core.*
import org.ton.lite.api.exception.LiteServerException
import org.ton.lite.api.liteserver.*
import org.ton.lite.api.liteserver.functions.*
import org.ton.tl.TlCodec

public interface LiteApiClient : LiteApi {
    public suspend fun <Q, A> sendQuery(
        queryCodec: TlCodec<Q>,
        answerCodec: TlCodec<A>,
        query: Q,
        seqno: Int = -1
    ): A {
        val rawQuery = buildPacket {
//            println("send query to lite server: $query")
            if (seqno >= 0) {
                val wait = LiteServerWaitMasterchainSeqno(seqno, 5000)
//                println("with prefix: $wait")
                LiteServerWaitMasterchainSeqno.encodeBoxed(this, wait)
            }
            queryCodec.encodeBoxed(this, query)
        }.readBytes()
        val liteServerQuery = LiteServerQuery(rawQuery)
        val result = sendRawQuery(buildPacket {
            LiteServerQuery.encodeBoxed(this, liteServerQuery)
        })
        val liteServerError = try {
            LiteServerError.decodeBoxed(result.copy())
        } catch (e: Exception) {
            null
        }
        if (liteServerError != null) {
            throw LiteServerException.create(liteServerError.code, liteServerError.message)
        }
        val answer = answerCodec.decodeBoxed(result)
        return answer
    }

    public suspend fun sendRawQuery(query: ByteReadPacket): ByteReadPacket

    override suspend fun invoke(function: LiteServerGetMasterchainInfo): LiteServerMasterchainInfo =
        sendQuery(LiteServerGetMasterchainInfo, LiteServerMasterchainInfo, function)

    override suspend fun invoke(function: LiteServerGetMasterchainInfoExt): LiteServerMasterchainInfoExt =
        sendQuery(LiteServerGetMasterchainInfoExt, LiteServerMasterchainInfoExt, function)

    override suspend fun invoke(function: LiteServerGetTime): LiteServerCurrentTime =
        sendQuery(LiteServerGetTime, LiteServerCurrentTime, function)

    override suspend fun invoke(function: LiteServerGetVersion): LiteServerVersion =
        sendQuery(LiteServerGetVersion, LiteServerVersion, function)

    override suspend fun invoke(function: LiteServerGetBlock): LiteServerBlockData =
        sendQuery(LiteServerGetBlock, LiteServerBlockData, function)

    override suspend fun invoke(function: LiteServerGetState): LiteServerBlockState =
        sendQuery(LiteServerGetState, LiteServerBlockState, function)

    override suspend fun invoke(function: LiteServerGetBlockHeader): LiteServerBlockHeader =
        sendQuery(LiteServerGetBlockHeader, LiteServerBlockHeader, function)

    override suspend fun invoke(function: LiteServerSendMessage): LiteServerSendMsgStatus =
        sendQuery(LiteServerSendMessage, LiteServerSendMsgStatus, function)

    override suspend fun invoke(function: LiteServerGetAccountState): LiteServerAccountState =
        sendQuery(LiteServerGetAccountState, LiteServerAccountState, function)

    override suspend fun invoke(function: LiteServerRunSmcMethod): LiteServerRunMethodResult =
        sendQuery(LiteServerRunSmcMethod, LiteServerRunMethodResult, function)

    override suspend fun invoke(function: LiteServerGetShardInfo): LiteServerShardInfo =
        sendQuery(LiteServerGetShardInfo, LiteServerShardInfo, function)

    override suspend fun invoke(function: LiteServerGetOneTransaction): LiteServerTransactionInfo =
        sendQuery(LiteServerGetOneTransaction, LiteServerTransactionInfo, function)

    override suspend fun invoke(function: LiteServerGetAllShardsInfo): LiteServerAllShardsInfo =
        sendQuery(LiteServerGetAllShardsInfo, LiteServerAllShardsInfo, function)

    override suspend fun invoke(function: LiteServerGetTransactions): LiteServerTransactionList =
        sendQuery(LiteServerGetTransactions, LiteServerTransactionList, function)

    override suspend fun invoke(function: LiteServerLookupBlock): LiteServerBlockHeader =
        sendQuery(LiteServerLookupBlock, LiteServerBlockHeader, function)

    override suspend fun invoke(function: LiteServerListBlockTransactions): LiteServerBlockTransactions =
        sendQuery(LiteServerListBlockTransactions, LiteServerBlockTransactions, function)

    override suspend fun invoke(function: LiteServerGetBlockProof): LiteServerPartialBlockProof =
        sendQuery(LiteServerGetBlockProof, LiteServerPartialBlockProof, function)

    override suspend fun invoke(function: LiteServerGetConfigAll): LiteServerConfigInfo =
        sendQuery(LiteServerGetConfigAll, LiteServerConfigInfo, function)

    override suspend fun invoke(function: LiteServerGetConfigParams): LiteServerConfigInfo =
        sendQuery(LiteServerGetConfigParams, LiteServerConfigInfo, function)

    override suspend fun invoke(function: LiteServerGetValidatorStats): LiteServerValidatorStats =
        sendQuery(LiteServerGetValidatorStats, LiteServerValidatorStats, function)
}
