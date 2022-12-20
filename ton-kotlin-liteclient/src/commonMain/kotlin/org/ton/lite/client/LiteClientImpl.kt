package org.ton.lite.client

import io.ktor.utils.io.core.*
import org.ton.adnl.connection.AdnlClient
import org.ton.lite.api.LiteApiClient
import org.ton.lite.api.liteserver.LiteServerError
import org.ton.lite.api.liteserver.functions.LiteServerQuery
import org.ton.lite.api.liteserver.functions.LiteServerWaitMasterchainSeqno
import org.ton.lite.api.exception.LiteServerException
import org.ton.tl.TlCodec
import kotlin.time.Duration.Companion.seconds

internal class LiteClientImpl(
    val adnlClient: AdnlClient
) : LiteApiClient {
    override suspend fun sendRawQuery(query: ByteReadPacket): ByteReadPacket  {
        return adnlClient.sendQuery(query, 10.seconds)
    }
}
