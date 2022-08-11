package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString

@Serializable
@SerialName("block_id_ext")
data class BlockIdExt(
    val shard_id: ShardIdent,
    val seq_no: Long,
    val root_hash: BitString,
    val file_hash: BitString
) {
    init {
        require(root_hash.size == 256) { "required: root_hash.size == 256, actual: ${root_hash.size}" }
        require(file_hash.size == 256) { "required: file_hash.size == 256, actual: ${file_hash.size}" }
    }
}
