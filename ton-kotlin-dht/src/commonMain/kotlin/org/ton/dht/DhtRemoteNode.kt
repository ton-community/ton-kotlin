package org.ton.dht

import org.ton.api.dht.DhtNode

class DhtRemoteNode(
    val dhtNode: DhtNode
) {
    val key get() = dhtNode.key()

}
