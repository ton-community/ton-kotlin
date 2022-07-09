@file:UseSerializers(HexByteArraySerializer::class)

package org.ton.api.tonnode

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.ton.bitstring.BitString
import org.ton.crypto.HexByteArraySerializer
import org.ton.crypto.encodeHex
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.*

@Serializable
data class TonNodeBlockIdExt(
    val workchain: Int,
    val shard: Long,
    val seqno: Int,
    val root_hash: ByteArray,
    val file_hash: ByteArray
) {
    constructor(
        workchain: Int,
        shard: Long,
        seqno: Int,
        root_hash: BitString,
        file_hash: BitString
    ) : this(workchain, shard, seqno, root_hash.toByteArray(), file_hash.toByteArray())

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TonNodeBlockIdExt

        if (workchain != other.workchain) return false
        if (shard != other.shard) return false
        if (seqno != other.seqno) return false
        if (!root_hash.contentEquals(other.root_hash)) return false
        if (!file_hash.contentEquals(other.file_hash)) return false

        return true
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
        append(shard.toULong().toString(16))
        append(":")
        append(seqno)
        append(" root_hash:")
        append(root_hash.encodeHex())
        append(" file_hash:")
        append(file_hash.encodeHex())
        append(")")
    }

    companion object : TlCodec<TonNodeBlockIdExt> by TonNodeBlockIdExtTlConstructor
}

private object TonNodeBlockIdExtTlConstructor : TlConstructor<TonNodeBlockIdExt>(
    type = TonNodeBlockIdExt::class,
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