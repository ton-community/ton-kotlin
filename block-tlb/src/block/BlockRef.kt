package org.ton.kotlin.block

import kotlinx.io.bytestring.ByteString
import org.ton.kotlin.shard.ShardIdent

/**
 * Reference to the external block.
 */
public data class BlockRef(
    /**
     * The end of the logical time of the referenced block.
     */
    val endLt: Long,
    /**
     * Sequence number of the referenced block.
     */
    val seqno: Int,
    /**
     * Representation hash of the root cell of the referenced block.
     */
    val rootHash: ByteString,
    /**
     * Hash of the BOC encoded root cell of the referenced block.
     */
    val fileHash: ByteString
) {
    /**
     * Converts a [BlockRef] to a [BlockId] given a shard identifier.
     */
    public fun toBlockId(shard: ShardIdent): BlockId =
        BlockId(shard, seqno, rootHash, fileHash)
}

