package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("counters")
data class Counters(
    val last_updated: Long,
    val total: Long,
    val cnt2048: Long,
    val cnt65536: Long
)