package org.ton.dht.network

import kotlinx.coroutines.Deferred

public interface Network {
    public val transport: Transport

    public fun listen(address: AdnlAddress): Deferred<Unit>

    public fun connect()
}

