package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.Cell

@Serializable
@SerialName("action_set_code")
data class ActionSetCode(
    val new_code: Cell
) : OutAction
