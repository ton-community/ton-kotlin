package org.ton.adnl.client.engine.cio

import io.ktor.client.utils.*
import io.ktor.utils.io.*
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    val answer = AdnlMessageAnswer.decodeBoxed(input)
    check(answer.query_id.contentEquals(query.query_id)) {
        "query_id mismatch, expected: ${query.query_id.encodeHex()} actual: ${answer.query_id.encodeHex()}"
    }
    return@withContext answer
}
