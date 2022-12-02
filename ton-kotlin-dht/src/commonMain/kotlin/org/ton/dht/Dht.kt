package org.ton.dht

import org.ton.api.adnl.AdnlIdShort
import org.ton.api.dht.config.DhtConfigGlobal
import org.ton.dht.storage.DhtStorage
import kotlin.coroutines.CoroutineContext

class Dht {
    companion object {
        fun client(
            id: AdnlIdShort,
            storage: DhtStorage,
            config: DhtConfigGlobal,
            coroutineContext: CoroutineContext
        ) {
            require(config.k > 0)
            require(config.a > 0)

        }
    }
}
