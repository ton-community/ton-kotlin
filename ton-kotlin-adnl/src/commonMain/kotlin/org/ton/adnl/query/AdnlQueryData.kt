package org.ton.adnl.query

import io.ktor.utils.io.core.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Job
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.ton.api.adnl.message.AdnlMessageAnswer
import org.ton.api.adnl.message.AdnlMessageQuery
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
