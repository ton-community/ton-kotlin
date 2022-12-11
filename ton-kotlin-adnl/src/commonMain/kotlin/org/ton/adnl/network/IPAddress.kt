package org.ton.adnl.network

import org.ton.adnl.ipv4
import org.ton.api.adnl.AdnlAddress
import org.ton.api.adnl.AdnlAddressUdp
import kotlin.jvm.JvmStatic

sealed interface IPAddress {
    val host: String
    val port: Int

    fun toAdnlAddress(): AdnlAddress

    companion object {
        @JvmStatic
        fun ipv4(host: String, port: Int): IPAddress = IPv4Address(host, port)
        @JvmStatic
        fun ipv4(address: Int, port: Int): IPAddress = IPv4Address(address, port)
    }
}

data class IPv4Address(
    val address: Int,
    override val port: Int,
) : IPAddress {
    constructor(host: String, port: Int) : this(ipv4(host), port)

    init {
        require(port in 0..0xFFFF) { "Invalid port: $port" }
    }

    override val host: String
        get() = ipv4(address)

    override fun toAdnlAddress(): AdnlAddressUdp = AdnlAddressUdp(address, port)

    override fun toString(): String = "$host:$port"
}
