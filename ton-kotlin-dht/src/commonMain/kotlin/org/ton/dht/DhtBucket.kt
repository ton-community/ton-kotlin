package org.ton.dht

import org.ton.api.dht.DhtNode
import org.ton.bitstring.BitString

class DhtBucket(
    k: Int
) {
    private val activeNodes = ArrayList<DhtRemoteNode>(k)
    private val backupNodes = ArrayList<DhtRemoteNode>(k)

    val activeCount get() = activeNodes.size

    fun getNearestNode(
        id: BitString,
        k: Int
    ): List<DhtNode> {
        if (activeNodes.size == 0) return emptyList()

        val map = HashMap<BitString, Int>()
        for (i in activeNodes.indices) {
            val node = activeNodes[i]
            val distance = id xor node.key
            map[distance] = i
        }

        return map.entries.asSequence()
            .sortedBy { it.key }
            .map { activeNodes[it.value].dhtNode }
            .take(k)
            .toList()
    }

    companion object {
        const val PING_TIMEOUT_MS = 60_000L
        const val MAX_MISSED_PINGS = 3L
    }
}
