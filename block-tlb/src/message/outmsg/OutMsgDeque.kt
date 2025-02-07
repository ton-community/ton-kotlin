package org.ton.kotlin.message.outmsg

import org.ton.kotlin.cell.CellRef
import org.ton.kotlin.message.envelope.MsgEnvelope

/**
 * ```tlb
 * msg_export_deq$1100 out_msg:^MsgEnvelope
 *     import_block_lt:uint63 = OutMsg;
 * ```
 */
public data class OutMsgDeque(
    val outMsg: CellRef<MsgEnvelope>,
    val importBlockLt: Long
) : OutMsg