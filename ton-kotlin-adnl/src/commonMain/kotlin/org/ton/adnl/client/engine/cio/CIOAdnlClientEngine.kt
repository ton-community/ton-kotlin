package org.ton.adnl.client.engine.cio

import io.ktor.client.engine.*
import io.ktor.client.utils.*
import io.ktor.network.selector.*
import io.ktor.util.*
import io.ktor.util.collections.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ClosedSendChannelException
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.isActive
import org.ton.adnl.client.engine.AdnlClientEngine
import org.ton.adnl.client.engine.AdnlClientEngineBase
import org.ton.adnl.client.engine.AdnlClientEngineFactory
import org.ton.adnl.client.socket.ConnectionFactory
import org.ton.api.adnl.message.AdnlMessageAnswer
import org.ton.api.adnl.message.AdnlMessageQuery
import org.ton.api.liteserver.LiteServerDesc

class CIOAdnlClientEngine(
    override val config: CIOAdnlClientEngineConfig
) : AdnlClientEngineBase("adnl-cio") {
    @OptIn(InternalAPI::class)
    override val dispatcher: CoroutineDispatcher by lazy {
        Dispatchers.clientDispatcher(config.threadCount, "adnl-cio-dispatcher")
    }
    private val endpoints = ConcurrentMap<LiteServerDesc, Endpoint>()
    private val selectorManager = SelectorManager(dispatcher)
    private val connectionFactory = ConnectionFactory(selectorManager, config.maxConnectionsCount)

    override suspend fun query(
        liteServer: LiteServerDesc,
        adnlMessageQuery: AdnlMessageQuery
    ): AdnlMessageAnswer = coroutineScope {
        val callContext = coroutineContext
        while (coroutineContext.isActive) {
            val endpoint = selectEndpoint(liteServer)
            try {
                return@coroutineScope endpoint.execute(adnlMessageQuery, callContext)
            } catch (cause: ClosedSendChannelException) {
                continue
            } finally {
                if (!coroutineContext.isActive) {
                    endpoint.close()
                }
            }
        }
        throw ClientEngineClosedException()
    }

    private fun selectEndpoint(liteServerDesc: LiteServerDesc): Endpoint {
        return endpoints.computeIfAbsent(liteServerDesc) {
            Endpoint(liteServerDesc, config, connectionFactory, coroutineContext, onDone = {
                endpoints.remove(liteServerDesc)
            })
        }
    }

    companion object : AdnlClientEngineFactory<CIOAdnlClientEngineConfig> {
        override fun create(block: CIOAdnlClientEngineConfig.() -> Unit): AdnlClientEngine =
            CIOAdnlClientEngine(CIOAdnlClientEngineConfig().apply(block))
    }
}
