package org.ton.adnl.utils

import io.ktor.network.sockets.*
import org.ton.adnl.ipv4
import org.ton.api.adnl.AdnlAddressUdp

fun AdnlAddressUdp.toSocketAddress() = InetSocketAddress(ipv4(ip), port)
fun InetSocketAddress.toAdnlUdpAddress() = AdnlAddressUdp(ipv4(hostname), port)
