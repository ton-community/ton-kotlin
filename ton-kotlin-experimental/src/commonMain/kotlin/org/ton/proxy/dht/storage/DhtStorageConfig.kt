package org.ton.proxy.dht.storage

import kotlinx.serialization.Serializable
import org.ton.proxy.dht.DhtConfig

@Serializable
data class DhtStorageConfig(
    val maxKeyNameLength: Int,
    val maxKeyIndex: Int,
    val allowUnsignedValues: Boolean = false
) {
    constructor(dhtConfig: DhtConfig) : this(
        maxKeyNameLength = dhtConfig.maxKeyNameLength,
        maxKeyIndex = dhtConfig.maxKeyIndex
    )
}
