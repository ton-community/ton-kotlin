package org.ton.lite.client

import kotlinx.coroutines.asCoroutineDispatcher
import org.ton.adnl.client.AdnlTcpClient
import org.ton.lite.api.JvmLiteApi
import java.util.concurrent.ExecutorService
import kotlin.coroutines.CoroutineContext

class JvmLiteClient(
    adnlTcpClient: AdnlTcpClient,
    override val executorService: ExecutorService
) : LiteClient(adnlTcpClient), JvmLiteApi {
    override val coroutineContext: CoroutineContext = executorService.asCoroutineDispatcher()
}
