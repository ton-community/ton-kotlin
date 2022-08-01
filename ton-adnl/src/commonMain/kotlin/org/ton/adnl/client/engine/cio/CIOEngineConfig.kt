package org.ton.adnl.client.engine.cio

import org.ton.adnl.client.AdnlConfigBuilder
import org.ton.adnl.client.engine.AdnlClientEngineConfig

class CIOEngineConfig : AdnlClientEngineConfig() {
    val adnl = AdnlConfigBuilder()

    var maxConnectionsCount: Int = 1000
    var queryTimeout: Long = 15000

    fun adnl(block: AdnlConfigBuilder.() -> Unit): AdnlConfigBuilder = adnl.apply(block)
}
