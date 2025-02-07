package org.ton.kotlin.message.outmsg

import org.ton.kotlin.cell.CellRef
import org.ton.kotlin.message.envelope.MsgEnvelope
import org.ton.kotlin.message.inmsg.InMsg
import org.ton.kotlin.transaction.Transaction

/**
 * Immediately processed internal outbound message.
 *
 * ```tlb
 * msg_export_imm$010 out_msg:^MsgEnvelope
 *     transaction:^Transaction reimport:^InMsg = OutMsg;
 * ```
 */
public data class OutMsgImmediate(
    val outMsg: CellRef<MsgEnvelope>,
    val transaction: CellRef<Transaction>,
    val reimport: CellRef<InMsg>
) : OutMsg