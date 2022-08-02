package org.ton.api.liteclient.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.liteserver.LiteServerDesc
import org.ton.api.validator.config.ValidatorConfigGlobal

@SerialName("liteclient.config.global")
@Serializable
data class LiteClientConfigGlobal(
    val liteservers: List<LiteServerDesc>,
    val validator: ValidatorConfigGlobal
)
