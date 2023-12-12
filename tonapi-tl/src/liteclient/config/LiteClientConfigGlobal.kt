package org.ton.api.liteclient.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.dht.config.DhtConfigGlobal
import org.ton.api.liteserver.LiteServerDesc
import org.ton.api.validator.config.ValidatorConfigGlobal

@SerialName("liteclient.config.global")
@Serializable
public data class LiteClientConfigGlobal(
    val dht: DhtConfigGlobal = DhtConfigGlobal(),
    @SerialName("liteservers")
    val liteServers: Collection<LiteServerDesc>,
    val validator: ValidatorConfigGlobal = ValidatorConfigGlobal()
)
