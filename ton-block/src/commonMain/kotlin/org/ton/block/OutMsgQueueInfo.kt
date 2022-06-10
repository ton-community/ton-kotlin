package org.ton.block

import kotlinx.serialization.Serializable

@Serializable
data class OutMsgQueueInfo(
    val out_queue: OutMsgQueue,
    val proc_info: ProcessedInfo,
    val ihr_pending: IhrPendingInfo
)
