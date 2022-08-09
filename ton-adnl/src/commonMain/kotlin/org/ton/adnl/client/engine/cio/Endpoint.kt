@file:Suppress("ThrowableNotThrown")

package org.ton.adnl.client.engine.cio

import io.ktor.client.engine.cio.*
import io.ktor.client.network.sockets.*
import io.ktor.client.utils.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.core.*
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.*
import kotlinx.datetime.Clock
import org.ton.adnl.client.socket.ConnectionFactory
import org.ton.adnl.client.socket.adnl
import org.ton.adnl.ipv4
import org.ton.api.adnl.message.AdnlMessageAnswer
import org.ton.api.adnl.message.AdnlMessageQuery
import org.ton.api.liteserver.LiteServerDesc
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration

internal class Endpoint(
    private val liteServerDesc: LiteServerDesc,
    private val config: CIOAdnlClientEngineConfig,
    private val connectionFactory: ConnectionFactory,
    override val coroutineContext: CoroutineContext,
    private val onDone: () -> Unit
) : CoroutineScope, Closeable {
    private val lastActivity = atomic(Clock.System.now())
    private val connections = atomic(0)
    private val maxEndpointIdleTime = config.endpoint.connectTimeout * 2
    private val timeout = launch(coroutineContext + CoroutineName("Endpoint timeout($liteServerDesc)")) {
        try {
            while (true) {
                val remaining = (lastActivity.value + maxEndpointIdleTime) - Clock.System.now()
                if (remaining <= Duration.ZERO) {
                    break
                }
                delay(remaining)
            }
        } catch (_: Throwable) {
        } finally {
            onDone()
        }
    }

    suspend fun execute(
        query: AdnlMessageQuery,
        callContext: CoroutineContext
    ): AdnlMessageAnswer {
        lastActivity.value = Clock.System.now()
        try {
            val connection = connect()
            val input = connection.input
            val output = connection.output.handleHalfClosed(
                callContext,
                false
            )
            callContext[Job]!!.invokeOnCompletion { cause ->
                val originCause = cause?.unwrapCancellationException()
                try {
                    input.cancel(originCause)
                    output.close(originCause)
                    connection.socket.close()
                    releaseConnection()
                } catch (_: Throwable) {
                }
            }

            val timeout = config.queryTimeout
            setupTimeout(callContext, this, timeout)

            writeQuery(query, output, callContext)
            return readAnswer(query, input, callContext)
        } catch (cause: Throwable) {
            throw cause
        }
    }

    private suspend fun connect(): Connection {
        val connectionAttempts = config.endpoint.connectAttempts
        val connectTimeout = config.endpoint.connectTimeout
        val socketTimeout = config.endpoint.socketTimeout
        var timeoutFails = 0

        connections.incrementAndGet()

        try {
            repeat(connectionAttempts) {
                val address = InetSocketAddress(ipv4(liteServerDesc.ip), liteServerDesc.port)

                val connect: suspend CoroutineScope.() -> Socket = {
                    connectionFactory.connect(address) {
                        this.socketTimeout = socketTimeout.inWholeMilliseconds
                    }
                }

                val socket = withTimeoutOrNull(connectTimeout, connect)
                if (socket == null) {
                    timeoutFails++
                    return@repeat
                }

                try {
                    return socket.adnl(coroutineContext) {
                        this.serverPublicKey = liteServerDesc.id
                    }.connection()
                } catch (cause: Throwable) {
                    try {
                        socket.close()
                    } catch (_: Throwable) {
                    }

                    connectionFactory.release()
                    throw cause
                }
            }
        } catch (cause: Throwable) {
            throw cause
        } finally {
            connections.decrementAndGet()
        }

        throw when (timeoutFails) {
            connectionAttempts -> ConnectTimeoutException(
                "Connect timeout has expired [server:${toString()} connect_timeout:$connectTimeout ms]"
            )

            else -> FailToConnectException()
        }
    }

    private fun releaseConnection() {
        connectionFactory.release()
        connections.decrementAndGet()
    }

    override fun toString(): String = liteServerDesc.toString()

    override fun close() {
        timeout.cancel()
    }
}

private fun ConnectTimeoutException(endpoint: Endpoint, timeout: Long) = ConnectTimeoutException(
    "Connect timeout has expired [server:$endpoint connect_timeout:$timeout ms]"
)

@Suppress("OPT_IN_USAGE")
private fun setupTimeout(callContext: CoroutineContext, endpoint: Endpoint, timeout: Long) {
    if (timeout == Long.MAX_VALUE || timeout == 0L) return

    val timeoutJob = GlobalScope.launch {
        delay(timeout)
        callContext.job.cancel("Request is timed out", ConnectTimeoutException(endpoint, timeout))
    }

    callContext.job.invokeOnCompletion {
        timeoutJob.cancel()
    }
}
