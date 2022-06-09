package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SerialName("capabilities")
@Serializable
data class GlobalVersion(
    val version: Int,
    val capabilities: Long
)