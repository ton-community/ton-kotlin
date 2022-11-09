package org.ton.proxy.adnl

import org.ton.api.adnl.AdnlIdShort
import org.ton.api.pk.PrivateKey
import org.ton.api.pub.PublicKey
import org.ton.crypto.encodeHex
import org.ton.tl.TLFunction
import org.ton.tl.TlCodec
import org.ton.tl.TlObject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

interface AdnlSender {
    suspend fun message(
        destination: AdnlIdShort,
        payload: ByteArray
    )

    suspend fun query(
        destination: AdnlIdShort,
        payload: ByteArray,
        timeout: Duration = 5.seconds,
        maxAnswerSize: Long = Int.MAX_VALUE.toLong()
    ): ByteArray

    @Suppress("UNCHECKED_CAST")
    suspend fun <Q : TLFunction<Q, A>, A: TlObject<A>> query(
        destination: AdnlIdShort,
        query: Q,
        timeout: Duration = 5.seconds,
        maxAnswerSize: Long = Int.MAX_VALUE.toLong()
    ): A {
        val queryCodec = query.tlCodec() as TlCodec<Q>
        val answerCodec = query.resultTlCodec()

        val queryPayload = queryCodec.encodeBoxed(query)
        val answerPayload = query(destination, queryPayload, timeout, maxAnswerSize)

        return try {
            answerCodec.decodeBoxed(answerPayload)
        } catch (e: Exception) {
            throw IllegalStateException("Failed to decode answer: ${answerPayload.encodeHex()}", e)
        }
    }

    fun createPeer(
        remoteKey: PublicKey,
        localKey: PrivateKey
    ): AdnlPeerSession
}
