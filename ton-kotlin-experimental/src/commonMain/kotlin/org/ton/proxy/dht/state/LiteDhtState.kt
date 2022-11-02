package org.ton.proxy.dht.state

import org.ton.proxy.dht.DhtBucket
import org.ton.proxy.dht.DhtPeer
import org.ton.proxy.dht.storage.DhtStorage

class LiteDhtState(
    knownPeers: MutableSet<DhtPeer> = HashSet()
) : AbstractDhtState(knownPeers) {
    override val buckets: DhtBucket? get() = null
    override val storage: DhtStorage? get() = null
}
