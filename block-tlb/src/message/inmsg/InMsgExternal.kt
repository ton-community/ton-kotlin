package org.ton.kotlin.message.inmsg

import org.ton.kotlin.cell.CellRef
import org.ton.kotlin.cell.CellSlice
import org.ton.kotlin.message.Message
import org.ton.kotlin.message.info.MsgInfo
import org.ton.kotlin.transaction.Transaction

/**
 * Inbound external message.
 *
 * ```tlb
 * msg_import_ext$000 msg:^(Message Any) transaction:^Transaction
 *   = InMsg;
 * ```
 */
public data class InMsgExternal(
    /**
     * External message itself.
     */
    val msg: CellRef<Message<MsgInfo, CellSlice>>,

    /**
     * Executed transaction for this external message.
     */
    val transaction: CellRef<Transaction>
) : InMsg