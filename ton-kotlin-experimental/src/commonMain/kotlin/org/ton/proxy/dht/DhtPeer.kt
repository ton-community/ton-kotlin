package org.ton.proxy.dht

import kotlinx.datetime.Clock
import org.ton.api.adnl.AdnlAddressList
import org.ton.api.adnl.AdnlAddressUdp
import org.ton.api.adnl.AdnlIdShort
import org.ton.api.dht.*
import org.ton.api.dht.functions.DhtFindValue
import org.ton.api.dht.functions.DhtPing
import org.ton.proxy.adnl.AdnlPeer

class DhtPeer private constructor(
    val dhtNode: DhtNode,
    val addresses: List<AdnlAddressUdp>,
) : AdnlPeer(addresses.first(), dhtNode.id) {
    /**
     * `dht.ping random_id:long = dht.Pong;`
     */
    suspend fun ping(long: Long = Clock.System.now().epochSeconds): DhtPong = query(DhtPing(long))

    /**
     * `dht.findValue key:int256 k:int = dht.ValueResult;`
     */
    suspend fun findValue(key: ByteArray, k: Int = 6): DhtValueResult = query(DhtFindValue(key, k))
    suspend fun findValue(key: DhtKey, k: Int = 6): DhtValueResult = query(DhtFindValue(key, k))

    suspend fun findAddress(adnlIdShort: AdnlIdShort): Pair<AdnlAddressList?, DhtNodes?> {
        return when(val result = findValue(DhtKey.address(adnlIdShort), 6)) {
            is DhtValueFound -> AdnlAddressList.decodeBoxed(result.value.value) to null
            is DhtValueNotFound -> null to result.nodes
        }
    }

    companion object {
        @JvmStatic
        fun from(dhtNode: DhtNode): DhtPeer? {
            val udpAddresses = dhtNode.addr_list.addrs.filterIsInstance<AdnlAddressUdp>()
            if (udpAddresses.isEmpty()) return null
            return DhtPeer(dhtNode, udpAddresses)
        }
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun DhtPeer(dhtNode: DhtNode): DhtPeer? = DhtPeer.from(dhtNode)
