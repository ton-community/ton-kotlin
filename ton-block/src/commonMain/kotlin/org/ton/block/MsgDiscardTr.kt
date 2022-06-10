package org.ton.block

import org.ton.cell.Cell

data class MsgDiscardTr(
    val in_msg: MsgEnvelope,
    val transaction_id: Long,
    val fwd_fee: Coins,
    val proof_delivered: Cell
) : InMsg
