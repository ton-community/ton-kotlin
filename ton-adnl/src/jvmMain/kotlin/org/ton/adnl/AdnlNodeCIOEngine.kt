package org.ton.adnl

import io.ktor.client.engine.*
import io.ktor.client.utils.*
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.util.*
import kotlinx.coroutines.*
import org.ton.adnl.node.AdnlNodeEngineBase
import org.ton.adnl.node.AdnlRequestData
import org.ton.adnl.node.AdnlResponseData
import kotlin.coroutines.CoroutineContext

@OptIn(InternalAPI::class, ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
class AdnlNodeCIOEngine(
    val config: Config
) : AdnlNodeEngineBase("adnl-node-cio") {
    override val dispatcher by lazy {
        Dispatchers.clientDispatcher(config.threadCount, "adnl-node-cio-dispatcher")
    }
    private val selectorManager by lazy {
        SelectorManager(dispatcher)
    }
    private val requestJob: CoroutineContext
    override val coroutineContext: CoroutineContext

    init {
        val parentContext = super.coroutineContext
        val parent = parentContext[Job]!!

        requestJob = SilentSupervisor(parent)

        val requestField = requestJob
        coroutineContext = parentContext + requestField

        val requestJob = requestField[Job]!!
        val selector = selectorManager

        GlobalScope.launch(parentContext, start = CoroutineStart.ATOMIC) {
            try {
                requestJob.join()
            } finally {
                selector.close()
                selector.coroutineContext[Job]!!.join()
            }
        }
    }

    override suspend fun execute(data: AdnlRequestData): AdnlResponseData {
        val callContext = callContext()
        while (coroutineContext.isActive) {

        }
        TODO("Not yet implemented")
    }

    fun connect(
        socketAddress: SocketAddress,
        configuration: SocketOptions.UDPSocketOptions.() -> Unit = {}
    ) = aSocket(selectorManager).udp().bind(socketAddress, configuration)

    data class Config(
        val threadCount: Int = 4
    )
}