@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@JsonClassDiscriminator("@type")
@Serializable
sealed interface BlkPrevInfo

@SerialName("prev_blk_info")
@Serializable
data class PrevBlkInfo(
    val prev: ExtBlkRef
) : BlkPrevInfo

@SerialName("prev_blks_info")
@Serializable
data class PrevBlksInfo(
    val prev1: ExtBlkRef,
    val prev2: ExtBlkRef
) : BlkPrevInfo