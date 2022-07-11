package org.ton.adnl.node

import io.ktor.utils.io.*
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

internal class AdnlQueryContextElement(
    val callContext: CoroutineContext
) : CoroutineContext.Element {
    override val key: CoroutineContext.Key<*>
        get() = AdnlQueryContextElement

    companion object : CoroutineContext.Key<AdnlQueryContextElement>
}

@OptIn(InternalCoroutinesApi::class)
internal suspend fun attackToUserJob(callJob: Job) {
    val userJob = coroutineContext[Job] ?: return
    val cleanupHandler = userJob.invokeOnCompletion(onCancelling = true) { cause ->
        cause ?: return@invokeOnCompletion
        callJob.cancel(CancellationException(cause.message))
    }
    callJob.invokeOnCompletion {
        cleanupHandler.dispose()
    }
}