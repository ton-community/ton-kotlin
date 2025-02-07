package org.ton.kotlin.message.inmsg

import org.ton.kotlin.cell.CellRef
import org.ton.kotlin.currency.Coins
import org.ton.kotlin.message.envelope.MsgEnvelope
import org.ton.kotlin.transaction.Transaction

/**
 * msg_import_imm$011 in_msg:^MsgEnvelope
 *     transaction:^Transaction fwd_fee:Coins = InMsg;
 */
public data class InMsgImmediate(
    /**
     * Old envelope.
     */
    val inMsg: CellRef<MsgEnvelope>,

    /**
     * Transaction
     */
    val transaction: CellRef<Transaction>,

    /**
     * Forward fee.
     */
    val fwdFee: Coins
) : InMsg