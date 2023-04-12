package org.ton.adnl.connection

import io.ktor.utils.io.core.*
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withTimeout
import org.ton.api.adnl.message.AdnlMessageAnswer
import org.ton.api.adnl.message.AdnlMessageQuery
import org.ton.api.liteserver.LiteServerDesc
import org.ton.tl.ByteString
import org.ton.tl.asByteString
import kotlin.random.Random
import kotlin.time.Duration

public class AdnlClientImpl(
    private val liteServerDesc: LiteServerDesc
) : AdnlClient {
    override suspend fun sendQuery(data: ByteReadPacket, timeout: Duration): ByteReadPacket {
        val adnlConnection = connectionPool.selectConnection(liteServerDesc)
        val queryId = ByteString.of(*Random.nextBytes(32))
        val context = SupervisorJob()
        val queryData = data.readBytes()
        try {
            return withTimeout(timeout) {
                val response = adnlConnection.execute(
                    AdnlRequestData(
                        buildPacket {
                            AdnlMessageQuery.encodeBoxed(
                                this, AdnlMessageQuery(queryId, queryData.asByteString())
                            )
                        }.readBytes(),
                        context
                    ), context
                )
                ByteReadPacket(
                    AdnlMessageAnswer.decodeBoxed(
                        response.body
                    ).answer.toByteArray()
                )
            }
        } catch (e: Throwable) {
            throw e
        }
    }

    public companion object {
        private val connectionPool: AdnlConnectionPool = AdnlConnectionPool()
    }
}
