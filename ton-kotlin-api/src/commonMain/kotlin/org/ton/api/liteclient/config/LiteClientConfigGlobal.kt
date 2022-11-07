package org.ton.api.liteclient.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.dht.config.DhtConfigGlobal
import org.ton.api.liteserver.LiteServerDesc
import org.ton.api.validator.config.ValidatorConfigGlobal

@SerialName("liteclient.config.global")
@Serializable
data class LiteClientConfigGlobal(
    val dht: DhtConfigGlobal = DhtConfigGlobal(),
    val liteservers: List<LiteServerDesc> = emptyList(),
    val validator: ValidatorConfigGlobal = ValidatorConfigGlobal()
)
