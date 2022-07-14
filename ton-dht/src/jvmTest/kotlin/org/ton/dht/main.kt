package org.ton.dht

import io.ktor.network.sockets.*
import kotlinx.coroutines.delay
import org.ton.adnl.ipv4
import org.ton.adnl.node.AdnlNodeEngineCIO
import org.ton.adnl.utils.toSocketAddress
import org.ton.api.adnl.AdnlAddressList
import org.ton.api.adnl.AdnlAddressUdp
import org.ton.api.adnl.AdnlPacketContents
import org.ton.api.adnl.message.AdnlMessage
import org.ton.api.adnl.message.AdnlMessageCreateChannel
import org.ton.api.adnl.message.AdnlMessageQuery
import org.ton.api.dht.DhtNode
import org.ton.api.dht.DhtNodes
import org.ton.api.dht.DhtPing
import org.ton.api.dht.functions.DhtQuery
import org.ton.api.pk.PrivateKeyEd25519
import org.ton.api.pub.PublicKeyEd25519
import org.ton.crypto.base64
import kotlin.random.Random

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
//val DHT_NODES = DhtNodes(
//    nodes = listOf(
//        DhtNode(
//            id = PublicKeyEd25519(base64("D/w54kkxf7n03EWJfTzjawGO4czuybDU21TfVQippeE=")),
//            addr_list = AdnlAddressList(
//                addrs = listOf(
//                    AdnlAddressUdp(
//                        ip = 1091921385,
//                        port = 33668
//                    )
//                ),
//                version = 0,
//                reinit_date = 0,
//                priority = 0,
//                expire_at = 0
//            ),
//            version = -1,
//            signature = base64("+LyFiXYiqqmjbNP89bNmDLU3YwDidVY3KW967jHsCCVxdGdceyPNiasgoDME7bbV5NC0+9CQEuUjOj9UJafLCg==")
//        )
//    )
//)
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

suspend fun main() {
    dhtQuery()
    delay(10000)
}

val localKey = PrivateKeyEd25519(ByteArray(32))
val otherKey = ADNL_NODES.first().id
val engine = AdnlNodeEngineCIO().apply {
    val socket = aSocket(selectorManager)
        .udp()
        .connect(
            (DHT_NODES.first().addr_list.first() as AdnlAddressUdp).toSocketAddress()
        )
    start(socket)
}

suspend fun createChannel() {
    packet(
        AdnlMessageCreateChannel(

        )
    )
}

suspend fun dhtQuery() {
    val dhtNode = DHT_NODES.first()
    println(ipv4(-1307380867))
    val packet = packet(
        AdnlMessageQuery(
            Random.nextBytes(32),
            DhtQuery.encodeBoxed(DhtQuery(dhtNode)) +
                    DhtPing.encodeBoxed(DhtPing(1))
        )
    )
    engine.sendPacket(packet, otherKey)
    val packetContents = engine.receivePacket()
    println(packetContents)
}

fun packet(message: AdnlMessage): AdnlPacketContents {
    return AdnlPacketContents(
        from = localKey.publicKey(),
        from_short = localKey.publicKey().toAdnlIdShort(),
        message = message,
        address = DHT_NODES.first().addr_list
    )
}

