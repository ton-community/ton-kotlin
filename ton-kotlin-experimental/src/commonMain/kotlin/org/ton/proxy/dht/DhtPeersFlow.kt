package org.ton.proxy.dht

import kotlinx.coroutines.flow.AbstractFlow
import kotlinx.coroutines.flow.FlowCollector

class DhtPeersFlow : AbstractFlow<DhtPeer>() {
    override suspend fun collectSafely(collector: FlowCollector<DhtPeer>) {

    }
}
