package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString

@SerialName("block_extra")
@Serializable
data class BlockExtra(
    val in_msg_descr: InMsgDescr,
    val out_msg_descr: OutMsgDescr,
    val account_blocks: ShardAccountBlocks,
    val rand_seed: BitString,
    val created_by: BitString,
    val custom: Maybe<McBlockExtra>
) {
    init {
        require(rand_seed.size == 256) { "expected: rand_seed.size == 256, actual: ${rand_seed.size}" }
        require(created_by.size == 256) { "expected: created_by.size == 256, actual: ${created_by.size}" }
    }
}