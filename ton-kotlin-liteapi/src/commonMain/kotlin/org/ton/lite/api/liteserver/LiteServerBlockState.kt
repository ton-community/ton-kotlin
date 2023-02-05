package org.ton.lite.api.liteserver

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.tl.*

@Serializable
@SerialName("liteServer.blockState")
public data class LiteServerBlockState(
    val id: TonNodeBlockIdExt,
    @SerialName("root_hash")
    val rootHash: ByteArray,
    @SerialName("file_hash")
    val fileHash: ByteArray,
    val data: ByteArray
) {
    init {
        require(rootHash.size == 32) { "rootHash must be 32 bytes long" }
        require(fileHash.size == 32) { "fileHash must be 32 bytes long" }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LiteServerBlockState) return false

        if (id != other.id) return false
        if (!rootHash.contentEquals(other.rootHash)) return false
        if (!fileHash.contentEquals(other.fileHash)) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + rootHash.contentHashCode()
        result = 31 * result + fileHash.contentHashCode()
        result = 31 * result + data.contentHashCode()
        return result
    }

    public companion object : TlCodec<LiteServerBlockState> by LiteServerBlockStateTlConstructor
}

private object LiteServerBlockStateTlConstructor : TlConstructor<LiteServerBlockState>(
    schema = "liteServer.blockState id:tonNode.blockIdExt root_hash:int256 file_hash:int256 data:bytes = liteServer.BlockState"
) {
    override fun decode(reader: TlReader): LiteServerBlockState {
        val id = reader.read(TonNodeBlockIdExt)
        val rootHash = reader.readRaw(32)
        val fileHash = reader.readRaw(32)
        val data = reader.readBytes()
        return LiteServerBlockState(id, rootHash, fileHash, data)
    }

    override fun encode(writer: TlWriter, value: LiteServerBlockState) {
        writer.write(TonNodeBlockIdExt, value.id)
        writer.writeRaw(value.rootHash)
        writer.writeRaw(value.fileHash)
        writer.writeBytes(value.data)
    }
}
