package org.ton.proxy.dht.state

import io.ktor.util.collections.*
import org.ton.api.adnl.AdnlIdShort
import org.ton.proxy.dht.DhtBucket
import org.ton.proxy.dht.DhtPeer
import org.ton.proxy.dht.storage.DhtStorage

abstract class AbstractDhtState(
    override val knownPeers: MutableSet<DhtPeer>
) : DhtState {
    abstract val buckets: DhtBucket?
    abstract val storage: DhtStorage?

    val penalties = ConcurrentMap<AdnlIdShort, Int>()

    override fun addPeer(peer: DhtPeer): AdnlIdShort {
        val peerKey = peer.key
        val peerId = peerKey.toAdnlIdShort()

        if (knownPeers.add(peer)) {
            peer.start()
            buckets?.set(peerId, peer.dhtNode)
        } else {
            setGoodPeer(peerId)
        }

        return peerId
    }

    override fun updatePeerStatus(peer: AdnlIdShort, isGood: Boolean) {
        if (isGood) {
            setGoodPeer(peer)
        } else {
            val value = penalties[peer]
            if (value == null) {
                penalties[peer] = 0
            } else {
                penalties[peer] = value + 2
            }
        }
    }

    override fun setGoodPeer(peer: AdnlIdShort) {
        val value = penalties[peer] ?: return
        if (value <= 1) {
            penalties.remove(peer)
        } else {
            penalties[peer] = value - 1
        }
    }
}
