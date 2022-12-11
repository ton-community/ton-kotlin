package org.ton.adnl.connection

import io.ktor.utils.io.core.*
import kotlinx.coroutines.CoroutineScope
import kotlin.time.Duration

interface AdnlClient : CoroutineScope {
    suspend fun sendQuery(data: ByteReadPacket, timeout: Duration): ByteReadPacket
}
