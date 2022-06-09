package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SerialName("master_info")
@Serializable
data class BlkMasterInfo(
    val master: ExtBlkRef
)