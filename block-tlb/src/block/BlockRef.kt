package org.ton.block.block

import kotlinx.io.bytestring.ByteString
import org.ton.block.ShardIdent
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbConstructor

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

    public object Tlb : TlbConstructor<BlockRef>(
        schema = "ext_blk_ref\$_ end_lt:uint64 " +
                "seq_no:uint32 root_hash:bits256 file_hash:bits256 " +
                "= ExtBlkRef;"
    ) {
        override fun storeTlb(
            cellBuilder: CellBuilder,
            value: BlockRef
        ): Unit = cellBuilder {
            storeUInt64(value.endLt.toULong())
            storeUInt32(value.seqno.toUInt())
            storeByteString(value.rootHash)
            storeByteString(value.fileHash)
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): BlockRef = cellSlice {
            val endLt = loadULong().toLong()
            val seqNo = loadUInt().toInt()
            val rootHash = loadByteString(256)
            val fileHash = loadByteString(256)
            BlockRef(endLt, seqNo, rootHash, fileHash)
        }
    }
}

