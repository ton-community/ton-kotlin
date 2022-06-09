@file:Suppress("NOTHING_TO_INLINE")

package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.Cell

inline fun MsgImportExt(
    msg: Message<Cell>,
    transaction: Transaction
) = MsgImportExt.of(msg, transaction)

interface MsgImportExt : InMsg {
    val msg: Message<Cell>
    val transaction: Transaction

    companion object {
        @JvmStatic
        fun of(
            msg: Message<Cell>,
            transaction: Transaction
        ): MsgImportExt = MsgImportExtData(msg, transaction)
    }
}

@Serializable
@SerialName("msg_import_ext")
private data class MsgImportExtData(
    override val msg: Message<Cell>,
    override val transaction: Transaction
) : MsgImportExt, InMsgData