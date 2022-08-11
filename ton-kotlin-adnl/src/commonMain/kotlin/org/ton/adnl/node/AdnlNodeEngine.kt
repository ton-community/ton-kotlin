package org.ton.adnl.node

import io.ktor.util.*
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.*
import org.ton.adnl.packet.AdnlPacket
import org.ton.api.adnl.AdnlPacketContents
import java.io.Closeable
import kotlin.coroutines.CoroutineContext

interface AdnlNodeEngine : CoroutineScope, Closeable {
    val dispatcher: CoroutineDispatcher

    private val closed: Boolean
        get() = !(coroutineContext[Job]?.isActive ?: false)

    fun start()

    suspend fun sendPacket(packet: AdnlPacket)

    suspend fun receivePacket(): AdnlPacketContents
}

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

@OptIn(ExperimentalCoroutinesApi::class)
private fun CoroutineDispatcher.close() {
    try {
        when (this) {
            is CloseableCoroutineDispatcher -> close()
            is io.ktor.utils.io.core.Closeable -> close()
        }
    } catch (ignore: Throwable) {
        // Some closeable dispatchers like Dispatchers.IO can't be closed.
    }
}