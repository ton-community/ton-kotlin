package org.ton.lite.client

import kotlinx.coroutines.Dispatchers
import org.ton.adnl.AdnlPublicKey
import org.ton.adnl.AdnlTcpClient
import org.ton.adnl.AdnlTcpClientImpl
import org.ton.adnl.ipv4
import org.ton.lite.api.LiteApi
import org.ton.logger.Logger
import org.ton.logger.PrintLnLogger

open class LiteClient(
    val adnlTcpClient: AdnlTcpClient
) : LiteApi {
    constructor(
        host: String,
        port: Int,
        publicKey: ByteArray,
        logger: Logger = PrintLnLogger("TON LiteClient")
    ) : this(AdnlTcpClientImpl(host, port, AdnlPublicKey(publicKey), Dispatchers.Default, logger))

    override val logger get() = adnlTcpClient.logger

    constructor(ipv4: Int, port: Int, publicKey: ByteArray, logger: Logger = PrintLnLogger("TON LiteClient")) : this(
        ipv4(ipv4),
        port,
        publicKey,
        logger
    )

    suspend fun connect() = apply {
        adnlTcpClient.connect()
    }

    override suspend fun sendRawQuery(byteArray: ByteArray): ByteArray =
        adnlTcpClient.sendQuery(byteArray)


}
