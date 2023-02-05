package org.ton.tonlib.block

import org.ton.block.KeyExtBlkRef
import org.ton.block.KeyMaxLt
import org.ton.hashmap.AugDictionary
import org.ton.tonlib.BlockSeqno

internal class ConfigInfo(
    val globalId: Int,
    val blockId: BlockIdExt,
    val vertSeqno: BlockSeqno,
    val utime: UInt,
    val lt: ULong,
    val minRefMcSeqno: BlockSeqno,
) {
    fun checkOldMcBlockId(
        blkId: BlockIdExt, strict: Boolean
    ) = if (!strict && blkId.id.seqno == blockId.id.seqno && blockId.isValid()) {
        blkId == blockId
    } else {
        TODO()
    }

    private fun checkOldMcBlockId(
        prevBlocksDictionary: AugDictionary<KeyExtBlkRef, KeyMaxLt>,
        blkId: BlockIdExt
    ) = checkOldMcBlockId(blkId, false)
}
