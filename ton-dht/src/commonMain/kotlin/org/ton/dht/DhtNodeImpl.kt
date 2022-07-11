package org.ton.dht

import io.ktor.util.collections.*
import kotlinx.atomicfu.AtomicInt
import org.ton.adnl.node.AdnlNodeImpl
import org.ton.adnl.node.AdnlPeer
import org.ton.api.adnl.AdnlNode
import org.ton.api.pub.PublicKey
import org.ton.logger.Logger
import org.ton.logger.PrintLnLogger

class DhtNodeImpl(
    private val adnl: AdnlNodeImpl,
    private val logger: Logger = PrintLnLogger("DHT Node")
) {
    private val buckets = ConcurrentMap<Int, ConcurrentMap<PublicKey, AdnlNode>>()
    private val badPeers = ConcurrentMap<PublicKey, AtomicInt>()
    private val knownPeers = AdnlAddressCache(limit = MAX_PEERS).also {
        it.logger.level = logger.level
    }

    fun addPeer(peer: AdnlPeer): Boolean {
        if (adnl.addPeer(peer) == null) return false
        val nodeId = peer.node.id
        if (knownPeers.put(nodeId)) {
            val affinity = adnl.currentPeer.node.id affinity peer.node.id
            val bucket = buckets.getOrPut(affinity) { ConcurrentMap() }
            val oldNode = bucket[nodeId]
            if ((oldNode?.addrList?.version ?: -1) < peer.node.addrList.version) {
                bucket[nodeId] = peer.node
            }
        } else {
            setGoodPeer(peer)
        }
        return true
    }

    fun setGoodPeer(peer: AdnlPeer) = setGoodPeer(peer.node.id)
    fun setGoodPeer(peer: PublicKey) {
        while (true) {
            val count = badPeers[peer]
            if (count != null) {
                val countValue = count.value
                if (count.compareAndSet(countValue, countValue - 1)) {
                    logger.info { "Make DHT peer $peer feel good ${countValue - 1}" }
                    break
                }
            } else {
                break
            }
        }
    }

    companion object {
        val MAX_PEERS = 65536
        val MAX_FAIL_COUNT = 5
    }
}