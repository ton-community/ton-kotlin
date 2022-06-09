package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SerialName("block_extra")
@Serializable
data class BlockExtra(
    val in_msg_descr: InMsgDescr,
//    val out_msg_descr: OutMsgDescr,
//    val account_blocks: ShardAccountBlocks,
//    val rand_seed: BitString
//    val created_by: BitString,
//    val custom: Maybe<McBlockExtra>
) {
    init {
        TODO()
    }
}