package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("msg_export_imm")
data class MsgExportImm(
    val out_msg: MsgEnvelope,
    val transaction: Transaction,
    val reimport: InMsg
) : OutMsg