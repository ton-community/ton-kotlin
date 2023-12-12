package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.Cell

@Serializable
@SerialName("action_send_msg")
public data class ActionSendMsg(
    val mode: Int,
    val outMsg: MessageRelaxed<Cell>
) : OutAction
