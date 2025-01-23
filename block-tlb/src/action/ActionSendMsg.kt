package org.ton.block.action

import org.ton.block.message.MessageRelaxed
import org.ton.cell.Cell
public data class ActionSendMsg(
    val mode: Int,
    val outMsg: MessageRelaxed<Cell>
) : OutAction
