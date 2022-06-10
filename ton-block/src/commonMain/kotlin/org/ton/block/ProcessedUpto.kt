package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString

@Serializable
@SerialName("processed_upto")
data class ProcessedUpto(
    val last_msg_lt: Long,
    val last_msg_hash: BitString
) {
    init {
        require(last_msg_hash.size == 256) { "required: last_msg_hash.size == 256, actual: ${last_msg_hash.size}" }
    }
}
