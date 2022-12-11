package org.ton.adnl.connection

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.util.collections.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import org.ton.adnl.network.IPAddress
import org.ton.adnl.query.AdnlQueryId
import org.ton.adnl.query.AdnlQueryTask
import org.ton.api.adnl.message.AdnlMessageQuery
import org.ton.api.pk.PrivateKey
import org.ton.api.pub.PublicKey
import org.ton.bitstring.BitString
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration

open class AdnlClientImpl(
    coroutineContext: CoroutineContext,
    val remoteAddress: IPAddress,
    val remoteKey: PublicKey,
    val localKey: PrivateKey? = null
) : AdnlClient {
    override val coroutineContext: CoroutineContext = coroutineContext + CoroutineName("AdnlClient")
    private val queries = ConcurrentMap<AdnlQueryId, AdnlQueryTask>()
    private val queriesTasks = Channel<AdnlQueryTask>(Channel.UNLIMITED)

    private val job = launch {
        var connection: AdnlOutboundConnection? = null
        for (task in queriesTasks) {
            queries[task.id] = task
            val currentConnection = connection ?: initConnection {
                connection = null
            }.also {
                it.handshakeJob.join()
            }
//            println("send query ${task.id}")
            currentConnection.send {
                AdnlMessageQuery.encodeBoxed(
                    this,
                    AdnlMessageQuery(
                        query_id = task.id.value,
                        query = task.query.readBytes()
                    )
                )
            }
        }
    }

    private suspend fun initConnection(onDone: () -> Unit): AdnlOutboundConnection {
        val socketConnection = aSocket(SelectorManager(coroutineContext)).tcp()
            .connect(remoteAddress.host, remoteAddress.port)
            .connection()
        return AdnlOutboundConnection(
            coroutineContext,
            socketConnection.input,
            socketConnection.output,
            this,
            remoteKey,
            localKey,
            onDone
        )
    }

    override suspend fun sendQuery(data: ByteReadPacket, timeout: Duration): ByteReadPacket {
        val queryId = generateNextQueryId()
        val answer = CompletableDeferred<ByteReadPacket>()
        val queryTask = AdnlQueryTask(
            queryId,
            data,
            answer,
            SupervisorJob()
        )
        try {
            return withTimeout(timeout) {
                queriesTasks.send(queryTask)
                answer.await()
            }
        } catch (e: Throwable) {
            queryTask.answer.cancel("Failed handle query", e)
            throw e
        }
    }

    private fun generateNextQueryId(): AdnlQueryId {
        while (true) {
            val id = AdnlQueryId()
            if (!queries.containsKey(id)) {
                return id
            }
        }
    }

    fun answerQuery(id: AdnlQueryId, data: ByteReadPacket) {
        queries.remove(id)?.answer?.complete(data)
    }
}
