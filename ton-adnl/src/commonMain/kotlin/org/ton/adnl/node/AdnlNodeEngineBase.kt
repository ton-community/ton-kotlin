package org.ton.adnl.node

import io.ktor.util.*
import io.ktor.utils.io.core.*
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

abstract class AdnlNodeEngineBase(
    private val engineName: String
) : AdnlNodeEngine {
    private val closed = atomic(false)

    override val coroutineContext: CoroutineContext by lazy {
        SilentSupervisor() + dispatcher + CoroutineName("$engineName-context")
    }

    override fun close() {
        if (!closed.compareAndSet(false, true)) return

        val requestJob = coroutineContext[Job] as? CompletableJob ?: return

        requestJob.complete()
        requestJob.invokeOnCompletion {
            dispatcher.close()
        }
    }
}

/**
 * Close [CoroutineDispatcher] if it's [CloseableCoroutineDispatcher] or [Closeable].
 */
@OptIn(ExperimentalCoroutinesApi::class)
private fun CoroutineDispatcher.close() {
    try {
        when (this) {
            is CloseableCoroutineDispatcher -> close()
            is Closeable -> close()
        }
    } catch (ignore: Throwable) {
        // Some closeable dispatchers like Dispatchers.IO can't be closed.
    }
}