package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("msg_export_tr")
data class MsgExportTr(
    val out_msg: MsgEnvelope,
    val imported: InMsg
) : OutMsg