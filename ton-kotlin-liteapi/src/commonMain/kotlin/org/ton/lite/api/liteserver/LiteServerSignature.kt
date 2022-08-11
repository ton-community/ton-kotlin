package org.ton.lite.api.liteserver

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readBytesTl
import org.ton.tl.constructors.readInt256Tl
import org.ton.tl.constructors.writeBytesTl
import org.ton.tl.constructors.writeInt256Tl

@Serializable
data class LiteServerSignature(
    val node_id_short: ByteArray,
    val signature: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LiteServerSignature

        if (!node_id_short.contentEquals(other.node_id_short)) return false
        if (!signature.contentEquals(other.signature)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = node_id_short.contentHashCode()
        result = 31 * result + signature.contentHashCode()
        return result
    }

    companion object : TlCodec<LiteServerSignature> by LiteServerSignatureTlConstructor
}

private object LiteServerSignatureTlConstructor : TlConstructor<LiteServerSignature>(
    type = LiteServerSignature::class,
    schema = "liteServer.signature node_id_short:int256 signature:bytes = liteServer.Signature"
) {
    override fun decode(input: Input): LiteServerSignature {
        val nodeIdShort = input.readInt256Tl()
        val signature = input.readBytesTl()
        return LiteServerSignature(nodeIdShort, signature)
    }

    override fun encode(output: Output, value: LiteServerSignature) {
        output.writeInt256Tl(value.node_id_short)
        output.writeBytesTl(value.signature)
    }
}