package org.ton.lite.api.liteserver

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.tl.*

@Serializable
@SerialName("liteServer.signature")
public data class LiteServerSignature(
    @SerialName("node_id_short")
    val nodeIdShort: ByteArray,
    val signature: ByteArray
) {
    init {
        require(nodeIdShort.size == 32) { "nodeIdShort must be 32 bytes long" }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LiteServerSignature) return false

        if (!nodeIdShort.contentEquals(other.nodeIdShort)) return false
        if (!signature.contentEquals(other.signature)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = nodeIdShort.contentHashCode()
        result = 31 * result + signature.contentHashCode()
        return result
    }

    public companion object : TlCodec<LiteServerSignature> by LiteServerSignatureTlConstructor
}

private object LiteServerSignatureTlConstructor : TlConstructor<LiteServerSignature>(
    schema = "liteServer.signature node_id_short:int256 signature:bytes = liteServer.Signature"
) {
    override fun decode(reader: TlReader): LiteServerSignature {
        val nodeIdShort = reader.readRaw(32)
        val signature = reader.readBytes()
        return LiteServerSignature(nodeIdShort, signature)
    }

    override fun encode(writer: TlWriter, value: LiteServerSignature) {
        writer.writeRaw(value.nodeIdShort)
        writer.writeBytes(value.signature)
    }
}
