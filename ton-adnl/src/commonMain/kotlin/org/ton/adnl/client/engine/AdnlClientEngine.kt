package org.ton.adnl.client.engine

import io.ktor.utils.io.core.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import org.ton.api.adnl.message.AdnlMessageAnswer
import org.ton.api.adnl.message.AdnlMessageQuery
import org.ton.api.liteserver.LiteServerDesc

interface AdnlClientEngine : CoroutineScope, Closeable {
    val dispatcher: CoroutineDispatcher

    val config: AdnlClientEngineConfig

    private val closed: Boolean
        get() = !(coroutineContext[Job]?.isActive ?: false)

    suspend fun query(
        liteServer: LiteServerDesc,
        query: ByteArray
    ): ByteArray =
//        query(liteServer, AdnlMessageQuery(Random.nextBytes(32), query)).answer
        query(liteServer, AdnlMessageQuery(ByteArray(32), query)).answer

    suspend fun query(
        liteServer: LiteServerDesc,
        adnlMessageQuery: AdnlMessageQuery
    ): AdnlMessageAnswer
}

interface AdnlClientEngineFactory<out T : AdnlClientEngineConfig> {
    fun create(block: T.() -> Unit = {}): AdnlClientEngine
}
