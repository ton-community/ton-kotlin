package org.ton.kotlin.message.inmsg

import org.ton.kotlin.cell.CellRef
import org.ton.kotlin.currency.Coins
import org.ton.kotlin.message.envelope.MsgEnvelope

public data class MsgDiscardFinal(
    val inMsg: CellRef<MsgEnvelope>,
    val transactionId: Long,
    val fwdFee: Coins
) : InMsg