package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.block.action.OutAction
import org.ton.cell.Cell

@Serializable
@SerialName("out_list_node")
public data class OutListNode(
    val prev: Cell,
    val action: OutAction
)
