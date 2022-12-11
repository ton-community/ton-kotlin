package org.ton.lite.client

import io.ktor.utils.io.core.*
import org.ton.lite.api.liteserver.LiteServerError
import org.ton.lite.api.liteserver.functions.LiteServerQuery
import org.ton.lite.api.liteserver.functions.LiteServerWaitMasterchainSeqno
import org.ton.lite.exception.LiteServerException
import org.ton.tl.TLFunction
import org.ton.tl.TlObject
import kotlin.time.Duration.Companion.seconds

class LiteClientImpl(
    val adnlClient: AdnlLazyClient
) {
    suspend fun <Q : TLFunction<Q, A>, A : TlObject<A>> sendQuery(
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
            query.tlCodec().encodeBoxed(this, query)
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
//            println("got error from lite server: $liteServerError")
            throw LiteServerException.create(liteServerError.code, liteServerError.message)
        }
        val answer = query.resultTlCodec().decodeBoxed(result)
//        println("got result from lite server: $answer")
        return answer
    }

    suspend fun sendRawQuery(query: ByteReadPacket): ByteReadPacket {
        return adnlClient.sendQuery(query, 10.seconds)
    }
}
