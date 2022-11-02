@file:Suppress("OPT_IN_USAGE")

package org.ton.proxy.dht

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import org.ton.api.dht.DhtNode
import org.ton.api.dht.DhtValueFound
import org.ton.api.dht.DhtValueNotFound
import org.ton.api.dht.DhtValueResult
import org.ton.api.dht.functions.DhtFindValue
import org.ton.logger.PrintLnLogger

class DhtValuesFlow(
    val key: ByteArray,
    val knownPeers: Set<DhtPeer>
) : AbstractFlow<Pair<DhtPeer, DhtValueResult?>>() {
    private val query = DhtFindValue(key, 6).toByteArray()
    private val peersChannel = Channel<DhtPeer>()
    private val logger = PrintLnLogger("DhtValuesFlow")

    companion object {
        const val QUERY_TIMEOUT = 5000L
        const val MAX_BATCH_COUNT = 5
    }

    @FlowPreview
    override suspend fun collectSafely(collector: FlowCollector<Pair<DhtPeer, DhtValueResult?>>): Unit =
        coroutineScope {
            launch {
                knownPeers.forEach { peer ->
                    addPeer(peer)
                }
            }
            while (isActive) {
                var batchCount = 0
                val results = peersChannel.receiveAsFlow().map { peer ->
                    async {
                        logger.debug { "Querying peer: $peer" }
                        val result = peerQuery(peer)
                        logger.debug { "Peer: $peer: $result" }
                        if (result is DhtValueNotFound) {
                            launch {
                                result.nodes.forEach { node ->
                                    addNode(node)
                                }
                            }
                        }
                        collector.emit(peer to result)
                        result
                    }
                }.takeWhile {
                    batchCount++
                    logger.debug { "Batch counter: $batchCount" }
                    batchCount >= MAX_BATCH_COUNT
                }.toList()
                if (results.any { it.await() is DhtValueFound }) {
                    logger.debug { "result found! stopping" }
                    break
                }
            }
        }

    private suspend fun peerQuery(peer: DhtPeer): DhtValueResult? {
        return withTimeoutOrNull(QUERY_TIMEOUT) {
            val rawResult = peer.rawQuery(query)
            DhtValueResult.decodeBoxed(rawResult)
        }
    }

    private suspend fun addNode(node: DhtNode) {
        val peer = DhtPeer(node)
        if (peer != null) {
            addPeer(peer)
        }
    }

    private suspend fun addPeer(peer: DhtPeer) {
        logger.debug { "Adding new peer: $peer" }
        peersChannel.send(peer)
    }
}
