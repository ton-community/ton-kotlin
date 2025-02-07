package org.ton.kotlin.message.inmsg

import org.ton.kotlin.cell.Cell
import org.ton.kotlin.cell.CellRef
import org.ton.kotlin.cell.CellSlice
import org.ton.kotlin.currency.Coins
import org.ton.kotlin.message.Message
import org.ton.kotlin.message.info.MsgInfo
import org.ton.kotlin.transaction.Transaction

/**
 *
 * ```tlb
 * msg_import_ihr$010 msg:^(Message Any) transaction:^Transaction
 *     ihr_fee:Coins proof_created:^Cell = InMsg;
 * ```
 */
public data class InMsgIhr(
    val msg: CellRef<Message<MsgInfo, CellSlice>>,
    val transaction: CellRef<Transaction>,
    val ihrFee: Coins,
    val proofCreated: Cell
) : InMsg
