package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.block.action.OutAction

@Serializable
@SerialName("out_list")
public data class OutListLink(
    val prev: OutList,
    val action: OutAction
) : OutList
