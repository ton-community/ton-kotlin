package org.ton.adnl.connection

import kotlin.time.Duration

public interface AdnlClient {
    public suspend fun sendQuery(data: ByteArray, timeout: Duration): ByteArray
}
