package org.ton.adnl.connection

import io.ktor.utils.io.core.*
import kotlin.time.Duration

public interface AdnlClient {
    public suspend fun sendQuery(data: ByteReadPacket, timeout: Duration): ByteReadPacket
}
