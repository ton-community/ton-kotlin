package org.ton.proxy

import org.ton.proxy.adnl.Peer
import org.ton.api.adnl.AdnlAddressUdp
import org.ton.api.adnl.message.AdnlMessageQuery
import org.ton.api.dht.DhtPing
import org.ton.api.pub.PublicKeyEd25519
import org.ton.crypto.base64

suspend fun main() {
    val address = AdnlAddressUdp(
        ip = -1307380867,
        port = 15888
    )
    val key = PublicKeyEd25519(
        base64("C1uy64rfGxp10SPSqbsxWhbumy5SM0YbvljCudwpZeI=")
    )
    val peer = Peer(
        address = address,
        key = key
    )
    peer.sendPacket(
        null,
        AdnlMessageQuery(
            query_id = ByteArray(32),
            query = DhtPing(0).toByteArray()
        )
    )
    while (true) {

    }
}
