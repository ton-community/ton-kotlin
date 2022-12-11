package org.ton.lite.client

import io.ktor.utils.io.core.*
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.ton.adnl.connection.AdnlClient
import org.ton.adnl.connection.AdnlClientImpl
import org.ton.adnl.network.IPAddress
import org.ton.api.liteclient.config.LiteClientConfigGlobal
import org.ton.api.liteserver.LiteServerDesc
import org.ton.api.pub.PublicKey
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

interface AdnlLazyClient : AdnlClient {
    fun forceChangeLiteServer()
}

class AdnlLazyClientImpl(
    coroutineContext: CoroutineContext,
    servers: Iterable<Pair<PublicKey, IPAddress>>
) : AdnlLazyClient {
    constructor(coroutineContext: CoroutineContext, globalLiteClientConfig: LiteClientConfigGlobal) :
            this(coroutineContext, globalLiteClientConfig.liteservers.map { it.id to IPAddress.ipv4(it.ip, it.port) })
    constructor(coroutineContext: CoroutineContext, remoteKey: PublicKey, remoteAddress: IPAddress) :
            this(coroutineContext, listOf(remoteKey to remoteAddress))

    init {
        require(servers.any()) { "No servers provided" }
    }

    override val coroutineContext: CoroutineContext = coroutineContext + CoroutineName("LazyClient")
    val servers = servers.shuffled()
    private var currentServerIndex = 0
    private var currentServerBad = false
    private var currentServerBadForce = false
    private var client: AdnlClient? = null
    private var closeClientAt: Instant = Instant.DISTANT_FUTURE

    private val job = launch {
        while (true) {
            val diff = closeClientAt - Clock.System.now()
            if (diff.isNegative()) {
                client?.cancel()
                client = null
            } else {
                delay(diff)
            }
        }
    }

    override fun forceChangeLiteServer() {
        if (servers.size == 1) return
        currentServerBad = true
        currentServerBadForce = true
    }

    override suspend fun sendQuery(data: ByteReadPacket, timeout: Duration): ByteReadPacket {
        beforeQuery()
        val client = client ?: throw IllegalStateException("Client is not initialized")
        val currentServerIndex = currentServerIndex
        try {
            return client.sendQuery(data, timeout)
        } catch (e: Exception) {
            setServerBad(currentServerIndex, true)
            throw e
        }
    }

    private fun beforeQuery() {
        closeClientAt = Clock.System.now() + MAX_NO_QUERIES_TIMEOUT
        if (currentServerBad) {
            currentServerIndex++
        } else if (client != null) {
            return
        }
        currentServerBad = false
        currentServerBadForce = false
        val server = servers[currentServerIndex % servers.size]
//        println("Connecting to ${server.second}")
        client = AdnlClientImpl(coroutineContext, server.second, server.first)
    }

    private fun setServerBad(index: Int, bad: Boolean) {
        if (index == currentServerIndex && servers.size > 1 && !currentServerBadForce) {
            currentServerBad = bad
        }
    }

    companion object {
        val MAX_NO_QUERIES_TIMEOUT = 100.seconds
    }
}
