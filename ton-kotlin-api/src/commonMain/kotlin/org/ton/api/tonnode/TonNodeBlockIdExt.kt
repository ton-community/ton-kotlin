@file:UseSerializers(HexByteArraySerializer::class)
@file:Suppress("NOTHING_TO_INLINE")

package org.ton.api.tonnode

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.ton.bitstring.Bits256
import org.ton.crypto.HexByteArraySerializer
import org.ton.crypto.hex
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.TlReader
import org.ton.tl.TlWriter
import kotlin.jvm.JvmStatic

@Serializable
public data class TonNodeBlockIdExt(
    override val workchain: Int,
    override val shard: Long,
    override val seqno: Int,
    @SerialName("root_hash")
    val rootHash: Bits256 = Bits256(),
    @SerialName("file_hash")
    val fileHash: Bits256 = Bits256(),
) : TonNodeBlockId {
    public constructor(
        tonNodeBlockId: TonNodeBlockId = TonNodeBlockId(),
    ) : this(
        tonNodeBlockId.workchain,
        tonNodeBlockId.shard,
        tonNodeBlockId.seqno,
    )

    public constructor(
        workchain: Int,
        shard: Long,
        seqno: Int,
        rootHash: ByteArray,
        fileHash: ByteArray
    ) : this(workchain, shard, seqno, Bits256(rootHash), Bits256(fileHash))

    public constructor(
        tonNodeBlockId: TonNodeBlockId,
        rootHash: Bits256,
        fileHash: Bits256,
    ) : this(
        tonNodeBlockId.workchain,
        tonNodeBlockId.shard,
        tonNodeBlockId.seqno,
        rootHash,
        fileHash
    )

    override fun toString(): String = buildString {
        append("(")
        append(workchain)
        append(":")
        append(shard.toULong().toString(16).uppercase())
        append(":")
        append(seqno)
        append(")")
        append(":")
        append(rootHash.hex())
        append(":")
        append(fileHash.hex())
    }

    public companion object : TlCodec<TonNodeBlockIdExt> by TonNodeBlockIdExtTlConstructor {
        @JvmStatic
        public fun parse(string: String): TonNodeBlockIdExt {
            require(string.getOrNull(0) == '(') { "Can't parse string: '$string'" }
            val closeParenIndex = string.indexOfFirst { it == ')' }
            require(closeParenIndex != -1) { "Can't parse string: '$string'" }
            val tonNodeBlockId = TonNodeBlockId.parse(string.substring(0, closeParenIndex + 1))
            val hashes = string.substring(closeParenIndex + 2, string.lastIndex + 1)
            val (rawRootHash, rawFileHash) = hashes.split(':')
            return TonNodeBlockIdExt(tonNodeBlockId, Bits256(hex(rawRootHash)), Bits256(hex(rawFileHash)))
        }

        @JvmStatic
        public fun parseOrNull(string: String): TonNodeBlockIdExt? = try {
            parse(string)
        } catch (e: Exception) {
            null
        }
    }
}

private object TonNodeBlockIdExtTlConstructor : TlConstructor<TonNodeBlockIdExt>(
    schema = "tonNode.blockIdExt workchain:int shard:long seqno:int root_hash:int256 file_hash:int256 = tonNode.BlockIdExt"
) {
    override fun decode(reader: TlReader): TonNodeBlockIdExt {
        val workchain = reader.readInt()
        val shard = reader.readLong()
        val seqno = reader.readInt()
        val rootHash = reader.readBits256()
        val fileHash = reader.readBits256()
        return TonNodeBlockIdExt(workchain, shard, seqno, rootHash, fileHash)
    }

    override fun encode(writer: TlWriter, value: TonNodeBlockIdExt) {
        writer.writeInt(value.workchain)
        writer.writeLong(value.shard)
        writer.writeInt(value.seqno)
        writer.writeBits256(value.rootHash)
        writer.writeBits256(value.fileHash)
    }
}
