package org.ton.block

import kotlinx.serialization.Serializable

@Serializable
data class KeyMaxLt(
    val key: Boolean,
    val max_end_lt: Long,
)
