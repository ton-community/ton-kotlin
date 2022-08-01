package org.ton.adnl.client.engine.cio

import io.ktor.client.utils.*
import io.ktor.util.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.ton.adnl.client.engine.AdnlClientEngineBase
import org.ton.adnl.client.engine.AdnlClientEngineConfig

internal class CIOEngine(
    override val config: AdnlClientEngineConfig
) : AdnlClientEngineBase("adnl-cio") {
    @OptIn(InternalAPI::class)
    override val dispatcher: CoroutineDispatcher by lazy {
        Dispatchers.clientDispatcher(config.threadCount, "adnl-cio-dispatcher")
    }
}
