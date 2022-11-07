package org.ton.adnl.utils

import io.ktor.network.sockets.*
import org.ton.adnl.ipv4
import org.ton.api.adnl.AdnlAddressUdp

fun AdnlAddressUdp.toSocketAddress() = InetSocketAddress(ipv4(ip), port)
fun InetSocketAddress.toAdnlUdpAddress(): AdnlAddressUdp {
    return if (hostname.count { it == '.' } == 3) {
        AdnlAddressUdp(ipv4(hostname), port)
    } else {
        AdnlAddressUdp(0, port)
    }
}
fun SocketAddress.toAdnlUdpAddress() = (this as InetSocketAddress).toAdnlUdpAddress()
