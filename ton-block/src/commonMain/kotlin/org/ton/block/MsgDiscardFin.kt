package org.ton.block

data class MsgDiscardFin(
    val in_msg: MsgEnvelope,
    val transaction_od: Long,
    val fwd_fee: Coins
) : InMsg
