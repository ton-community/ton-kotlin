package org.ton.adnl.connection

import org.ton.adnl.network.IPAddress
import org.ton.adnl.network.TcpClient
import org.ton.adnl.network.TcpClientImpl
import org.ton.api.liteserver.LiteServerDesc

class AdnlConnectionFactory {
    public suspend fun connect(
        liteServerDesc: LiteServerDesc
    ): TcpClient {
        try {
            return TcpClientImpl().also {
                val address = IPAddress.ipv4(liteServerDesc.ip, liteServerDesc.port)
                it.connect(address.host, address.port)
            }
        } catch (cause: Throwable) {
            throw cause
        }
    }
}
