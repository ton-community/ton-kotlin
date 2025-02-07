package org.ton.dht.network

public sealed interface AdnlAddress {

    public data class Udp(
        public val ip: String,
        public val port: Int
    ) : AdnlAddress

}