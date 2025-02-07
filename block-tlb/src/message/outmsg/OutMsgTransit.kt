package org.ton.kotlin.message.outmsg

import org.ton.kotlin.cell.CellRef
import org.ton.kotlin.message.envelope.MsgEnvelope
import org.ton.kotlin.message.inmsg.InMsg

/**
 * ```tlb
 * msg_export_tr$011 out_msg:^MsgEnvelope
 *     imported:^InMsg = OutMsg;
 * ```
 */
public data class OutMsgTransit(
    val outMsg: CellRef<MsgEnvelope>,
    val imported: CellRef<InMsg>
) : OutMsg