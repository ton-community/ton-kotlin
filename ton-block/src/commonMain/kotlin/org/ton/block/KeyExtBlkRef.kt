package org.ton.block

import kotlinx.serialization.Serializable

@Serializable
data class KeyExtBlkRef(
    val key: Boolean,
    val blk_ref: ExtBlkRef
)
