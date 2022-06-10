package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.Cell

@Serializable
@SerialName("action_send_msg")
data class ActionSendMsg(
    val mode: Int,
    val out_msg: MessageRelaxed<Cell>
) : OutAction
