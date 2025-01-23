package org.ton.block.block

import kotlinx.io.bytestring.ByteString
import kotlinx.io.bytestring.hexToByteString
import kotlinx.io.bytestring.toHexString
import org.ton.block.ShardIdent
import kotlin.jvm.JvmStatic

public data class BlockId(
    val shard: ShardIdent,
    val seqno: Int,
    val rootHash: ByteString,
    val fileHash: ByteString
) {
    val isMasterchain: Boolean get() = shard.isMasterchain

    override fun toString(): String =
        "$shard:$seqno:${rootHash.toHexString(HexFormat.UpperCase)}:${fileHash.toHexString(HexFormat.UpperCase)}"

    public companion object {
        @JvmStatic
        public fun parse(input: CharSequence): BlockId {
            val parts = input.split(':')
            if (parts.size != 4) {
                throw IllegalArgumentException("Invalid block id: $input")
            }
            val workchain = parts[0].toInt()
            val shardPrefix = parts[1].toULong(16)
            val shard = ShardIdent(workchain, shardPrefix)
            val seqno = parts[2].toInt()
            val rootHash = parts[3].hexToByteString()
            val fileHash = parts[4].hexToByteString()
            return BlockId(shard, seqno, rootHash, fileHash)
        }
    }
}