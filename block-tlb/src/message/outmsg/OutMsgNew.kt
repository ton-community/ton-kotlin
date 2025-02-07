package org.ton.kotlin.message.outmsg

import org.ton.kotlin.cell.CellRef
import org.ton.kotlin.message.envelope.MsgEnvelope
import org.ton.kotlin.transaction.Transaction

/**
 * Ordinary (internal) outbound message, generated in this block and included into the outbound queue.
 *
 * ```tlb
 * msg_export_new$001 out_msg:^MsgEnvelope
 *     transaction:^Transaction = OutMsg;
 * ```
 */
public data class OutMsgNew(
    /**
     * Outbound message envelope.
     */
    val outMsg: CellRef<MsgEnvelope>,

    /**
     * The source transaction of this message.
     */
    val transaction: CellRef<Transaction>
) : OutMsg