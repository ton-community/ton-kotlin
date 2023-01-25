package org.ton.tonlib.types

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("config")
internal data class Config(
    val config: String,
    @SerialName("blockchain_name")
    val blockchainName: String,
    @SerialName("use_callbacks_for_network")
    val useCallbacksForNetwork: Boolean,
    @SerialName("ignore_cache")
    val ignoreCache: Boolean
)
