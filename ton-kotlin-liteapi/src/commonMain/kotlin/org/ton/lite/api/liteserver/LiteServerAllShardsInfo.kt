package org.ton.lite.api.liteserver

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.tl.*

@Serializable
@SerialName("liteServer.allShardsInfo")
public data class LiteServerAllShardsInfo(
    val id: TonNodeBlockIdExt,
    val proof: ByteArray,
    val data: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LiteServerAllShardsInfo) return false

        if (id != other.id) return false
        if (!proof.contentEquals(other.proof)) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + proof.contentHashCode()
        result = 31 * result + data.contentHashCode()
        return result
    }

    public companion object : TlCodec<LiteServerAllShardsInfo> by LiteServerAllShardsInfoTlConstructor
}

private object LiteServerAllShardsInfoTlConstructor : TlConstructor<LiteServerAllShardsInfo>(
    schema = "liteServer.allShardsInfo id:tonNode.blockIdExt proof:bytes data:bytes = liteServer.AllShardsInfo"
) {
    override fun decode(reader: TlReader): LiteServerAllShardsInfo {
        val id = reader.read(TonNodeBlockIdExt)
        val proof = reader.readBytes()
        val data = reader.readBytes()
        return LiteServerAllShardsInfo(id, proof, data)
    }

    override fun encode(writer: TlWriter, value: LiteServerAllShardsInfo) {
        writer.write(TonNodeBlockIdExt, value.id)
        writer.writeBytes(value.proof)
        writer.writeBytes(value.data)
    }
}
