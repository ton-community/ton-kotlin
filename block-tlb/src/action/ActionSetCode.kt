package org.ton.block.action

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.Cell

@Serializable
@SerialName("action_set_code")
public data class ActionSetCode(
    val newCode: Cell
) : OutAction
