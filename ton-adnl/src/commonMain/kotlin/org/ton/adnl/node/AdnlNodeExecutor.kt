package org.ton.adnl.node

import io.ktor.events.*
import io.ktor.utils.io.core.*
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

class AdnlNodeExecutor(
    val engine: AdnlNodeEngine
) : CoroutineScope, Closeable {
    val closed = atomic(false)
    val nodeJob = Job(engine.coroutineContext[Job])
    val requestPipeline = AdnlRequestPipeline()
    val responsePipeline = AdnlResponsePipeline()
    val sendPipeline = AdnlSendPipeline()
    val receivePipeline = AdnlReceivePipeline()

    override val coroutineContext = engine.coroutineContext + nodeJob
    val monitor = Events()

    init {
        engine.install(this)

        sendPipeline.intercept(AdnlSendPipeline.RECEIVE) { query ->
            check(query is AdnlQuery) { "Error: AdnlQuery expected but found $query:(${query::class})" }
            val response = receivePipeline.execute(Unit, query.response)
            query.response = response
            proceedWith(query)
        }
        responsePipeline.intercept(AdnlResponsePipeline.RECEIVE) {
            try {
                proceed()
            } catch (cause: Throwable) {
                monitor.raise(AdnlResponseReceiveFailed, AdnlResponseReceiveFail(context.response, cause))
                throw cause
            }
        }
    }

    suspend fun execute(builder: AdnlRequestBuilder): AdnlQuery {
        monitor.raise(AdnlRequestCreated, builder)
        return requestPipeline.execute(builder, builder.body) as AdnlQuery
    }

    override fun close() {
        val success = closed.compareAndSet(false, true)
        if (!success) return

        nodeJob.complete()
    }

    override fun toString(): String = "AdnlNodeExecutor[$engine]"
}