package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("out_list")
data class OutListLink(
    val prev: OutList,
    val action: OutAction
) : OutList
