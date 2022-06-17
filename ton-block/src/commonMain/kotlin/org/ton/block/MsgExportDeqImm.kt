package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("msg_export_deq_imm")
data class MsgExportDeqImm(
    val out_msg: MsgEnvelope,
    val reimport: InMsg,
) : OutMsg