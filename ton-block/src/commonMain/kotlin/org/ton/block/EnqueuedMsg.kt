package org.ton.block

import kotlinx.serialization.Serializable

@Serializable
data class EnqueuedMsg(
    val enqueued_lt: Long,
    val out_msg: MsgEnvelope
)
