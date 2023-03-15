package org.ton.tonlib.types

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("ton.blockIdExt")
internal data class TonBlockIdExt(
    val workchain: Int,
    val shard: Long,
    val seqno: Int,
    val rootHash: String,
    val fileHash: String
)
