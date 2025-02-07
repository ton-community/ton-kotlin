package org.ton.kotlin.message.outmsg

import org.ton.kotlin.cell.CellRef
import org.ton.kotlin.message.envelope.MsgEnvelope
import org.ton.kotlin.message.inmsg.InMsg

/**
 * A message that was dequeued from the outbound queue and immediately queued in the same block.
 *
 * ```tlb
 * msg_export_deq_imm$100 out_msg:^MsgEnvelope
 *     reimport:^InMsg = OutMsg;
 * ```
 */
public data class OutMsgDequeImmediate(
    /**
     * Outbound message envelope.
     */
    val outMsg: CellRef<MsgEnvelope>,

    /**
     * The destination reimport message.
     */
    val reimport: CellRef<InMsg>,
) : OutMsg