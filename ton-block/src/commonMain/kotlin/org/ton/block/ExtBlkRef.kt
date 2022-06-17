package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString

@SerialName("ext_blk_ref")
@Serializable
data class ExtBlkRef(
    @SerialName("end_lt")
    val endLt: Long,
    @SerialName("seq_no")
    val seqNo: Int,
    @SerialName("root_hash")
    val rootHash: BitString,
    @SerialName("file_hash")
    val fileHash: BitString
) {
    init {
        require(rootHash.size == 256) { "expected: root_hash.size = 256, actual: ${rootHash.size}" }
        require(fileHash.size == 256) { "expected: fileHash.size = 256, actual: ${fileHash.size}" }
    }
}