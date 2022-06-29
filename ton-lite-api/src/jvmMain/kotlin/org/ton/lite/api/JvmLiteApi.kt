package org.ton.lite.api

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.future.asCompletableFuture
import org.ton.tl.TlConstructor
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService
import kotlin.coroutines.CoroutineContext

interface JvmLiteApi : LiteApi, CoroutineScope {
    val executorService: ExecutorService
    override val coroutineContext: CoroutineContext

    fun <Q : Any, A : Any> asyncSendQuery(
        query: Q,
        queryCodec: TlConstructor<Q>,
        answerCodec: TlConstructor<A>
    ): CompletableFuture<A> = async {
        query(query, queryCodec, answerCodec)
    }.asCompletableFuture()
}
