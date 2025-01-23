@file:Suppress("NOTHING_TO_INLINE")

package org.ton.api.tonnode

import kotlinx.io.bytestring.ByteString
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.tl.*
import kotlin.jvm.JvmName

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
    @Serializable(ByteStringBase64Serializer::class)
    val rootHash: ByteString,

    @SerialName("file_hash")
    @get:JvmName("fileHash")
    @Serializable(ByteStringBase64Serializer::class)
    val fileHash: ByteString
) : TonNodeBlockId {
    public companion object : TlCodec<TonNodeBlockIdExt> by TonNodeBlockIdExtTlConstructor
}

private object TonNodeBlockIdExtTlConstructor : TlConstructor<TonNodeBlockIdExt>(
    schema = "tonNode.blockIdExt workchain:int shard:long seqno:int root_hash:int256 file_hash:int256 = tonNode.BlockIdExt"
) {
    override fun decode(reader: TlReader): TonNodeBlockIdExt {
        val workchain = reader.readInt()
        val shard = reader.readLong()
        val seqno = reader.readInt()
        val rootHash = reader.readByteString(32)
        val fileHash = reader.readByteString(32)
        return TonNodeBlockIdExt(workchain, shard, seqno, rootHash, fileHash)
    }

    override fun encode(writer: TlWriter, value: TonNodeBlockIdExt) {
        writer.writeInt(value.workchain)
        writer.writeLong(value.shard)
        writer.writeInt(value.seqno)
        writer.writeRaw(value.rootHash)
        writer.writeRaw(value.fileHash)
    }
}
