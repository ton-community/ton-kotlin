package org.ton.adnl.node

import io.ktor.utils.io.core.*
import kotlinx.coroutines.*
import org.ton.adnl.exception.AdnlNodeEngineClosedException
import kotlin.coroutines.CoroutineContext

internal val QUERY_COROUTINE = CoroutineName("query-context")

interface AdnlNodeEngine : CoroutineScope, Closeable {
    val dispatcher: CoroutineDispatcher
    private val closed: Boolean
        get() = !(coroutineContext[Job]?.isActive ?: false)

    suspend fun execute(data: AdnlRequestData): AdnlResponseData

    fun install(node: AdnlNodeExecutor) {
        node.sendPipeline.intercept(AdnlSendPipeline.ENGINE) { content ->
            val builder = AdnlRequestBuilder().apply {
                takeFromWithExecutionContext(context)
                body = content as ByteArray
            }

            node.monitor.raise(AdnlRequestIsReadyForSending, builder)

            val requestData = builder.build()
            val responseData = executeWithinCallContext(requestData)
            val query = AdnlQuery(node, requestData, responseData)

            val response = query.response
            node.monitor.raise(AdnlResponseReceived, response)

            response.coroutineContext.job.invokeOnCompletion {
                if (it != null) {
                    node.monitor.raise(AdnlResponseCancelled, response)
                }
            }

            proceedWith(query)
        }
    }

    private suspend fun executeWithinCallContext(requestData: AdnlRequestData): AdnlResponseData {
        val callContext = createCallContext(requestData.executionContext)
        val context = callContext + AdnlQueryContextElement(callContext)
        return withContext(context) {
            if (closed) {
                throw AdnlNodeEngineClosedException()
            }
            execute(requestData)
        }
    }

    private suspend fun createCallContext(parentJob: Job): CoroutineContext {
        val callJob = Job(parentJob)
        val callContext = coroutineContext + callJob + QUERY_COROUTINE
        attackToUserJob(callJob)
        return callContext
    }

}