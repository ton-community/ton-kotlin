package org.ton.adnl.network

import kotlin.coroutines.CoroutineContext

internal expect class TcpClientImpl(
    coroutineContext: CoroutineContext,
) : TcpClient
