package org.ton.adnl.client.engine.cio

import io.ktor.client.engine.cio.*
import io.ktor.client.utils.*
import io.ktor.utils.io.*
import io.ktor.utils.io.CancellationException
import io.ktor.utils.io.errors.*
import kotlinx.coroutines.*
import org.ton.api.adnl.message.AdnlMessageAnswer
import org.ton.api.adnl.message.AdnlMessageQuery
import org.ton.crypto.encodeHex
import kotlin.coroutines.CoroutineContext

internal suspend fun writeQuery(
    query: AdnlMessageQuery,
    output: ByteWriteChannel,
    callContext: CoroutineContext,
    closeChannel: Boolean = true
) = withContext(callContext) {
    if (query.query.isEmpty()) {
        if (closeChannel) output.close()
        return@withContext
    }
    val scope = CoroutineScope(callContext + CoroutineName("Query writer"))
    scope.launch {
        try {
            output.writePacket {
                AdnlMessageQuery.encodeBoxed(
                    this,
                    query
                )
            }
        } catch (cause: Throwable) {
            output.close(cause)
            throw cause
        } finally {
            output.flush()
            output.closedCause?.unwrapCancellationException()?.takeIf { it !is CancellationException }?.let {
                throw it
            }
            if (closeChannel) {
                output.close()
            }
        }
    }
}

internal suspend fun readAnswer(
    query: AdnlMessageQuery,
    input: ByteReadChannel,
    callContext: CoroutineContext
): AdnlMessageAnswer = withContext(callContext) {
//    val answer = try {
//        AdnlMessageAnswer.decodeBoxed(input)
//    } catch (e: Exception) {
//        throw IOException("Can't parse ADNL answer for query: $query", e)
//    }
//    check(answer.query_id == query.query_id) {
//        "query_id mismatch, expected: ${query.query_id} actual: ${answer.query_id}"
//    }
//    return@withContext answer
    TODO()
}

/**
 * Wrap channel so that [ByteWriteChannel.close] of the resulting channel doesn't lead to closing of the base channel.
 */
@OptIn(DelicateCoroutinesApi::class)
internal fun ByteWriteChannel.withoutClosePropagation(
    coroutineContext: CoroutineContext,
    closeOnCoroutineCompletion: Boolean = true
): ByteWriteChannel {
    if (closeOnCoroutineCompletion) {
        // Pure output represents a socket output channel that is closed when request fully processed or after
        // request sent in case TCP half-close is allowed.
        coroutineContext[Job]!!.invokeOnCompletion {
            close()
        }
    }

    return GlobalScope.reader(coroutineContext, autoFlush = true) {
        channel.copyTo(this@withoutClosePropagation, Long.MAX_VALUE)
        this@withoutClosePropagation.flush()
    }.channel
}

/**
 * Wrap channel using [withoutClosePropagation] if [propagateClose] is false otherwise return the same channel.
 */
internal fun ByteWriteChannel.handleHalfClosed(
    coroutineContext: CoroutineContext,
    propagateClose: Boolean
): ByteWriteChannel = if (propagateClose) this else withoutClosePropagation(coroutineContext)
