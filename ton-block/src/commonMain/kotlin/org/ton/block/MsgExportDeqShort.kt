package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString

@Serializable
@SerialName("msg_export_deq_short")
data class MsgExportDeqShort(
    val msg_env_hash: BitString,
    val next_workchain: Int,
    val next_addr_pfx: Long,
    val import_block_lt: Long
) : OutMsg
