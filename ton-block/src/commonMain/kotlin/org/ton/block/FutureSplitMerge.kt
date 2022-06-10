@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@Serializable
@JsonClassDiscriminator("@type")
sealed interface FutureSplitMerge

@Serializable
@SerialName("fsm_none")
object FutureSplitMergeNone : FutureSplitMerge

@Serializable
@SerialName("fsm_split")
data class FutureSplitMergeSplit(
    val split_utime: Long,
    val interval: Long
)

@Serializable
@SerialName("fsm_merge")
data class FutureSplitMergeMerge(
    val merge_utime: Long,
    val interval: Long
)