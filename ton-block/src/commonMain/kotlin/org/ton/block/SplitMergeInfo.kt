package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString

@Serializable
@SerialName("split_merge_info")
data class SplitMergeInfo(
    val cur_shard_pfx_len: Int,
    val acc_split_depth: Int,
    val this_addr: BitString,
    val sibling_addr: BitString
) {
    init {
        require(this_addr.size == 256) { "required: this_addr.size == 256, actual: ${this_addr.size}" }
        require(sibling_addr.size == 256) { "required: sibling_addr.size == 256, actual: ${sibling_addr.size}" }
    }
}
