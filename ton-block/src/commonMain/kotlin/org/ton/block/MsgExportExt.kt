package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.Cell

@Serializable
@SerialName("msg_export_ext")
data class MsgExportExt(
    val msg: Message<Cell>,
    val transaction: Transaction
) : OutMsg