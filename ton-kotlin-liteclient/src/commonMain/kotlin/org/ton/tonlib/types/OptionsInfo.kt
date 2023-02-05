package org.ton.tonlib.types

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("options.info")
internal data class OptionsInfo(
    @SerialName("config_info")
    val configInfo: OptionsConfigInfo
)
