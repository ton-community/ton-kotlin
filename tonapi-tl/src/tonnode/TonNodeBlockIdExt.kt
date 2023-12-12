@file:Suppress("NOTHING_TO_INLINE")

package org.ton.api.tonnode

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.tl.*
import org.ton.tl.ByteString.Companion.decodeFromHex
import org.ton.tl.ByteString.Companion.toByteString
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic

@Serializable
public data class TonNodeBlockIdExt(
    @SerialName("workchain")
    @get:JvmName("workchain")
    override val workchain: Int,

    @SerialName("shard")
    @get:JvmName("shard")
    override val shard: Long,

    @SerialName("seqno")
    @get:JvmName("seqno")
    override val seqno: Int,

    @SerialName("root_hash")
    @get:JvmName("rootHash")
    val rootHash: ByteString,

    @SerialName("file_hash")
    @get:JvmName("fileHash")
    val fileHash: ByteString
) : TonNodeBlockId {
    public constructor(
        workchain: Int,
        shard: Long,
        seqno: Int,
        rootHash: ByteArray,
        fileHash: ByteArray
    ) : this(workchain, shard, seqno, rootHash.toByteString(), fileHash.toByteString())

    public constructor(
        tonNodeBlockId: TonNodeBlockId,
        rootHash: ByteArray,
        fileHash: ByteArray,
    ) : this(
        tonNodeBlockId.workchain,
        tonNodeBlockId.shard,
        tonNodeBlockId.seqno,
        rootHash,
        fileHash
    )

    public constructor(
        tonNodeBlockId: TonNodeBlockId = TonNodeBlockId(),
        rootHash: ByteString = ByteString.of(*ByteArray(32)),
        fileHash: ByteString = ByteString.of(*ByteArray(32)),
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
        append(rootHash.encodeHex())
        append(":")
        append(fileHash.encodeHex())
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
            return TonNodeBlockIdExt(tonNodeBlockId, rawRootHash.decodeFromHex(), rawFileHash.decodeFromHex())
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
        val rootHash = reader.readRaw(32)
        val fileHash = reader.readRaw(32)
        return TonNodeBlockIdExt(workchain, shard, seqno, rootHash, fileHash)
    }

    override fun encode(writer: TlWriter, value: TonNodeBlockIdExt) {
        writer.writeInt(value.workchain)
        writer.writeLong(value.shard)
        writer.writeInt(value.seqno)
        writer.writeRaw(value.rootHash.toByteArray())
        writer.writeRaw(value.fileHash.toByteArray())
    }
}
