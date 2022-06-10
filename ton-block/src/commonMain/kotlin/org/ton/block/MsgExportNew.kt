package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("msg_export_new")
data class MsgExportNew(
    val out_msg: MsgEnvelope,
    val transaction: Transaction
) : OutMsg