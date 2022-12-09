@file:UseSerializers(HexByteArraySerializer::class)
@file:Suppress("NOTHING_TO_INLINE")

package org.ton.api.tonnode

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.ton.api.tonnode.Workchain.INVALID_WORKCHAIN
import org.ton.bitstring.BitString
import org.ton.crypto.ByteArray
import org.ton.crypto.HexByteArraySerializer
import org.ton.crypto.base64.Base64ByteArraySerializer
import org.ton.crypto.encodeHex
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.*

inline fun TonNodeBlockIdExt(string: String): TonNodeBlockIdExt = TonNodeBlockIdExt.parse(string)

@Serializable
data class TonNodeBlockIdExt(
    override val workchain: Int = INVALID_WORKCHAIN,
    override val shard: Long = 0,
    override val seqno: Int = 0,
    @Serializable(Base64ByteArraySerializer::class)
    val root_hash: ByteArray = byteArrayOf(),
    @Serializable(Base64ByteArraySerializer::class)
    val file_hash: ByteArray = byteArrayOf()
) : TonNodeBlockId {
    constructor(
        workchain: Int,
        shard: Long,
        seqno: Int,
        root_hash: BitString,
        file_hash: BitString
    ) : this(workchain, shard, seqno, root_hash.toByteArray(), file_hash.toByteArray())

    constructor(
        tonNodeBlockId: TonNodeBlockId,
        root_hash: ByteArray = ByteArray(0),
        file_hash: ByteArray = ByteArray(0)
    ) : this(
        tonNodeBlockId.workchain,
        tonNodeBlockId.shard,
        tonNodeBlockId.workchain,
        root_hash, file_hash
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        when (other) {
            is TonNodeBlockIdExt -> {
                if (workchain != other.workchain) return false
                if (shard != other.shard) return false
                if (seqno != other.seqno) return false
                if (!root_hash.contentEquals(other.root_hash)) return false
                if (!file_hash.contentEquals(other.file_hash)) return false
                return true
            }

            is TonNodeBlockId -> {
                if (workchain != other.workchain) return false
                if (shard != other.shard) return false
                if (seqno != other.seqno) return false
                return true
            }

            else -> return false
        }
    }

    override fun hashCode(): Int {
        var result = workchain
        result = 31 * result + shard.hashCode()
        result = 31 * result + seqno
        result = 31 * result + root_hash.contentHashCode()
        result = 31 * result + file_hash.contentHashCode()
        return result
    }

    override fun toString(): String = buildString {
        append("(")
        append(workchain)
        append(":")
        append(shard.toULong().toString(16).uppercase())
        append(":")
        append(seqno)
        append(")")
        append(":")
        append(root_hash.encodeHex().uppercase())
        append(":")
        append(file_hash.encodeHex().uppercase())
    }

    companion object : TlCodec<TonNodeBlockIdExt> by TonNodeBlockIdExtTlConstructor {
        @JvmStatic
        fun parse(string: String): TonNodeBlockIdExt {
            require(string.getOrNull(0) == '(')
            val closeParenIndex = string.indexOfFirst { it == ')' }
            require(closeParenIndex != -1)
            val tonNodeBlockId = TonNodeBlockId.parse(string.substring(1, closeParenIndex))
            val (rawRootHash, rawFileHash) = string.substring(closeParenIndex, string.lastIndex).split(':')
            return TonNodeBlockIdExt(tonNodeBlockId, ByteArray(rawRootHash), ByteArray(rawFileHash))
        }

        @JvmStatic
        fun parseOrNull(string: String): TonNodeBlockIdExt? = try {
            parse(string)
        } catch (e: Exception) {
            null
        }
    }
}

private object TonNodeBlockIdExtTlConstructor : TlConstructor<TonNodeBlockIdExt>(
    schema = "tonNode.blockIdExt workchain:int shard:long seqno:int root_hash:int256 file_hash:int256 = tonNode.BlockIdExt"
) {
    override fun decode(input: Input): TonNodeBlockIdExt {
        val workchain = input.readIntTl()
        val shard = input.readLongTl()
        val seqno = input.readIntTl()
        val rootHash = input.readInt256Tl()
        val fileHash = input.readInt256Tl()
        return TonNodeBlockIdExt(workchain, shard, seqno, rootHash, fileHash)
    }

    override fun encode(output: Output, value: TonNodeBlockIdExt) {
        output.writeIntTl(value.workchain)
        output.writeLongTl(value.shard)
        output.writeIntTl(value.seqno)
        output.writeInt256Tl(value.root_hash)
        output.writeInt256Tl(value.file_hash)
    }
}
