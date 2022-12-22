package org.ton.adnl.query

import io.ktor.utils.io.core.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.datetime.Instant
import kotlin.coroutines.CoroutineContext


internal data class AdnlQueryTask(
    val id: AdnlQueryId,
    val query: ByteReadPacket,
    val answer: CompletableDeferred<ByteReadPacket>,
    val context: CoroutineContext
)

internal data class AdnlConnectionAnswerTask(
    val queryTime: Instant,
    val task: AdnlQueryTask
)
