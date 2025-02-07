package org.ton.kotlin.message.inmsg

import org.ton.kotlin.cell.CellRef
import org.ton.kotlin.currency.Coins
import org.ton.kotlin.message.envelope.MsgEnvelope

public data class InMsgTransit(
    val inMsg: CellRef<MsgEnvelope>,
    val outMsg: CellRef<MsgEnvelope>,
    val transitFee: Coins
) : InMsg