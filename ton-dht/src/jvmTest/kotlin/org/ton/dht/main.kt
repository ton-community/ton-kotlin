package org.ton.dht

import io.ktor.util.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import org.ton.adnl.node.AdnlNodeEngineCIO
import org.ton.api.adnl.AdnlAddressList
import org.ton.api.adnl.AdnlAddressUdp
import org.ton.api.adnl.AdnlPacketContents
import org.ton.api.adnl.message.AdnlMessageQuery
import org.ton.api.dht.DhtNode
import org.ton.api.dht.DhtNodes
import org.ton.api.dht.DhtPing
import org.ton.api.pk.PrivateKeyEd25519
import org.ton.api.pub.PublicKeyEd25519
import org.ton.crypto.base64

/*
{
    "@type": "dht.node",
    "id": {
        "@type": "pub.ed25519",
        "key": "D/w54kkxf7n03EWJfTzjawGO4czuybDU21TfVQippeE="
    },
    "addr_list": {
        "@type": "adnl.addressList",
        "addrs": [
            {
                "@type": "adnl.address.udp",
                "ip": 1091921385,
                "port": 33669
            }
        ],
        "version": 0,
        "reinit_date": 0,
        "priority": 0,
        "expire_at": 0
    },
    "version": -1,
    "signature": "+LyFiXYiqqmjbNP89bNmDLU3YwDidVY3KW967jHsCCVxdGdceyPNiasgoDME7bbV5NC0+9CQEuUjOj9UJafLCg=="
}
 */
val DHT_NODES = DhtNodes(
    nodes = listOf(
        DhtNode(
            id = PublicKeyEd25519(base64("D/w54kkxf7n03EWJfTzjawGO4czuybDU21TfVQippeE=")),
            addr_list = AdnlAddressList(
                addrs = listOf(
                    AdnlAddressUdp(
                        ip = 1091921385,
                        port = 33668
                    )
                ),
                version = 0,
                reinit_date = 0,
                priority = 0,
                expire_at = 0
            ),
            version = -1,
            signature = base64("+LyFiXYiqqmjbNP89bNmDLU3YwDidVY3KW967jHsCCVxdGdceyPNiasgoDME7bbV5NC0+9CQEuUjOj9UJafLCg==")
        )
    )
)
val ADNL_NODES = DHT_NODES.toAdnlNodes()

/*

 */

// Created symlink /etc/systemd/system/multi-user.target.wants/dht-server.service → /etc/systemd/system/dht-server.service.
suspend fun main() = coroutineScope {
    val engine = AdnlNodeEngineCIO().apply {
        start()
    }
    val otherKey = ADNL_NODES.first().id
    val localKey = PrivateKeyEd25519(ByteArray(32))
    println((-1946584576).toUInt().toString(2))
//    val adnl = AdnlNodeImpl(PrivateKeyEd25519(ByteArray(32)))
//    val dht = DhtNodeImpl(adnl)
//    val peer = AdnlPeer(
//        engine,
//        ADNL_NODES.first(),
//        Dispatchers.IO
//    )
//    println(base64(peer.node.id.toAdnlIdShort().id))
//    val pong = dht.ping(peer)
//    println(pong)
    val otherId = otherKey.toAdnlIdShort()
    println("otherKey: ${otherKey.toByteArray().encodeBase64()}")
    println("otherId: ${otherId.id.encodeBase64()}")

    // c183febcb0000000000000000000000
    val pingPayload = DhtPing.encodeBoxed(DhtPing(0))
    // 7af98bb400000000000000000000000000000000000000000000000000000000000000000c183febcb0000000000000000000000
    val adnlQuery = AdnlMessageQuery(ByteArray(32), pingPayload)

    val packet = AdnlPacketContents(
        rand1 = ByteArray(7) { 0xAA.toByte() },
        from = null,
        from_short = null,
        message = adnlQuery,
        messages = null,
        address = DHT_NODES.first().addr_list,
        priority_address = null,
        seqno = null,
        confirm_seqno = null,
        recv_addr_list_version = 0,
        recv_priority_addr_list_version = null,
        reinit_date = null,
        dst_reinit_date = null,
        signature = null,
        rand2 = ByteArray(7) { 0xAA.toByte() }
    )

    println("send...")
    engine.sendPacket(
        packet,
        otherKey
    )
    delay(9999)
}