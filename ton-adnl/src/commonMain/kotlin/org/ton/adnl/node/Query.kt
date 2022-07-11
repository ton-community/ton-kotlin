package org.ton.adnl.node

import kotlinx.atomicfu.atomic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

class AdnlQuery(
    val adnl: AdnlNodeExecutor
) : CoroutineScope {
    private val received = atomic(false)
    override val coroutineContext: CoroutineContext get() = response.coroutineContext

    lateinit var request: AdnlRequest
    lateinit var response: AdnlResponse

    constructor(
        node: AdnlNodeExecutor,
        requestData: AdnlRequestData,
        responseData: AdnlResponseData
    ) : this(node) {
        request = DefaultAdnlRequest(this, requestData)
        response = DefaultAdnlResponse(this, responseData)
    }
}

interface AdnlRequest : CoroutineScope {
    val query: AdnlQuery
    override val coroutineContext: CoroutineContext get() = query.coroutineContext
    val content: ByteArray
}

class AdnlRequestData(
    val body: ByteArray,
    val executionContext: Job
)

class DefaultAdnlRequest(
    override val query: AdnlQuery,
    data: AdnlRequestData
) : AdnlRequest {
    override val coroutineContext: CoroutineContext
        get() = query.coroutineContext
    override val content: ByteArray = data.body
}


class AdnlRequestBuilder {
    var body: ByteArray = ByteArray(0)
    var executionContext = SupervisorJob()

    fun build() = AdnlRequestData(body, executionContext)

    fun takeFromWithExecutionContext(builder: AdnlRequestBuilder): AdnlRequestBuilder {
        executionContext = builder.executionContext
        return takeFrom(builder)
    }

    fun takeFrom(builder: AdnlRequestBuilder): AdnlRequestBuilder {
        body = builder.body
        return this
    }
}

class AdnlResponseData(
    val body: ByteArray,
    val callContext: CoroutineContext,
)

abstract class AdnlResponse : CoroutineScope {
    abstract val query: AdnlQuery
    abstract val content: ByteArray
}

class DefaultAdnlResponse(
    override val query: AdnlQuery,
    responseData: AdnlResponseData
) : AdnlResponse() {
    override val coroutineContext: CoroutineContext = responseData.callContext
    override val content: ByteArray = responseData.body
}
