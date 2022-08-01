package org.ton.adnl.client.engine

import io.ktor.utils.io.core.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

interface AdnlClientEngine : CoroutineScope, Closeable {
    val dispatcher: CoroutineDispatcher

    val config: AdnlClientEngineConfig

    private val closed: Boolean
        get() = !(coroutineContext[Job]?.isActive ?: false)
}
