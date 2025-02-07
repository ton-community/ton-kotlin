package org.ton.kotlin.message.outmsg

import org.ton.kotlin.cell.CellRef
import org.ton.kotlin.cell.CellSlice
import org.ton.kotlin.message.Message
import org.ton.kotlin.message.info.MsgInfo
import org.ton.kotlin.transaction.Transaction

/**
 * External outbound message.
 *
 * ```tlb
 * msg_export_ext$000 msg:^(Message Any)
 *     transaction:^Transaction = OutMsg;
 * ```
 */
public data class OutMsgExternal(
    /**
     * External message itself.
     */
    val msg: CellRef<Message<MsgInfo, CellSlice>>,

    /**
     * The source transaction of this external message.
     */
    val transaction: CellRef<Transaction>
)