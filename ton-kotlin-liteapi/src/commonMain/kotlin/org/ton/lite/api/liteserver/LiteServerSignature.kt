package org.ton.lite.api.liteserver

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.tl.*

@Serializable
public data class LiteServerSignature(
    @SerialName("node_id_short")
    val nodeIdShort: Bits256,
    val signature: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LiteServerSignature) return false

        if (nodeIdShort != other.nodeIdShort) return false
        if (!signature.contentEquals(other.signature)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = nodeIdShort.hashCode()
        result = 31 * result + signature.contentHashCode()
        return result
    }

    public companion object : TlCodec<LiteServerSignature> by LiteServerSignatureTlConstructor
}

private object LiteServerSignatureTlConstructor : TlConstructor<LiteServerSignature>(
    schema = "liteServer.signature node_id_short:int256 signature:bytes = liteServer.Signature"
) {
    override fun decode(reader: TlReader): LiteServerSignature {
        val nodeIdShort = reader.readBits256()
        val signature = reader.readBytes()
        return LiteServerSignature(nodeIdShort, signature)
    }

    override fun encode(writer: TlWriter, value: LiteServerSignature) {
        writer.writeBits256(value.nodeIdShort)
        writer.writeBytes(value.signature)
    }
}
