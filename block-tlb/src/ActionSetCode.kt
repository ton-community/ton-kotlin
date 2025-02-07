package org.ton.block

import kotlinx.serialization.SerialName
import org.ton.cell.Cell


@SerialName("action_set_code")
public data class ActionSetCode(
    val newCode: Cell
) : OutAction
