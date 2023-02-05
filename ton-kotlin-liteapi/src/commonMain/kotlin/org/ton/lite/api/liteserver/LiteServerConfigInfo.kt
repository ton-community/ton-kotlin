package org.ton.lite.api.liteserver

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.tl.*

@Serializable
@SerialName("liteServer.configInfo")
public data class LiteServerConfigInfo(
    val mode: Int,
    val id: TonNodeBlockIdExt,
    val stateProof: ByteArray,
    val configProof: ByteArray
) {
    public companion object : TlCodec<LiteServerConfigInfo> by LiteServerConfigInfoTlConstructor

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LiteServerConfigInfo) return false

        if (mode != other.mode) return false
        if (id != other.id) return false
        if (!stateProof.contentEquals(other.stateProof)) return false
        if (!configProof.contentEquals(other.configProof)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = mode
        result = 31 * result + id.hashCode()
        result = 31 * result + stateProof.contentHashCode()
        result = 31 * result + configProof.contentHashCode()
        return result
    }
}

private object LiteServerConfigInfoTlConstructor : TlConstructor<LiteServerConfigInfo>(
    schema = "liteServer.configInfo mode:# id:tonNode.blockIdExt state_proof:bytes config_proof:bytes = liteServer.ConfigInfo"
) {
    override fun decode(reader: TlReader): LiteServerConfigInfo {
        val mode = reader.readInt()
        val id = reader.read(TonNodeBlockIdExt)
        val stateProof = reader.readBytes()
        val configProof = reader.readBytes()
        return LiteServerConfigInfo(mode, id, stateProof, configProof)
    }

    override fun encode(writer: TlWriter, value: LiteServerConfigInfo) {
        writer.writeInt(value.mode)
        writer.write(TonNodeBlockIdExt, value.id)
        writer.writeBytes(value.stateProof)
        writer.writeBytes(value.configProof)
    }
}
