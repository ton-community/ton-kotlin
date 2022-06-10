package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("msg_export_deq")
data class MsgExportDeq(
    val out_msg: MsgEnvelope,
    val import_block_lt: Long
) : OutMsg