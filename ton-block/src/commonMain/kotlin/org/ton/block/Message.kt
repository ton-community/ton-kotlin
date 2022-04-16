package org.ton.block

import kotlinx.serialization.Serializable

@Serializable
data class Message<T>(
    val info: CommonMsgInfo,
    val init: Pair<StateInit?, StateInit?>?,
    val body: Pair<T?, T?>
)