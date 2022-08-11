package org.ton.adnl.client.engine.cio

import org.ton.adnl.client.AdnlConfigBuilder
import org.ton.adnl.client.engine.AdnlClientEngineConfig
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class CIOAdnlClientEngineConfig : AdnlClientEngineConfig() {
    val adnl = AdnlConfigBuilder()

    /**
     * Provides access to [Endpoint] settings.
     */
    val endpoint = EndpointConfig()

    var maxConnectionsCount: Int = 1000

    /**
     * Specifies a time period (in milliseconds) required to process an ADNL query:
     * from sending a request to receiving first response bytes.
     *
     * To disable this timeout, set its value to `0`.
     */
    var queryTimeout: Long = 15000

    fun adnl(block: AdnlConfigBuilder.() -> Unit): AdnlConfigBuilder = adnl.apply(block)
}

class EndpointConfig {
    /**
     * Specifies a connection keep-alive time (in milliseconds).
     */
    var keepAliveTime: Duration = 5000.milliseconds

    /**
     * Specifies a time period (in milliseconds) in which a client should establish a connection with a server.
     */
    var connectTimeout: Duration = 5000.milliseconds

    /**
     * Specifies a maximum time (in milliseconds) of inactivity between two data packets when exchanging data with a server.
     */
    var socketTimeout: Duration = Duration.INFINITE

    /**
     * Specifies a maximum number of connection attempts.
     * Note: this property affects only connection retries, but not request retries.
     */
    var connectAttempts: Int = 1
}
