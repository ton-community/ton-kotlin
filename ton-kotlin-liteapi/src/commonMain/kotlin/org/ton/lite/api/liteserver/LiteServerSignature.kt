package org.ton.lite.api.liteserver

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.tl.*
import org.ton.tl.ByteString.Companion.toByteString

@Serializable
@SerialName("liteServer.signature")
public data class LiteServerSignature(
    @SerialName("node_id_short")
    val nodeIdShort: ByteString,
    val signature: ByteString
) {
    public constructor(nodeIdShort: ByteArray, signature: ByteArray) : this(nodeIdShort.toByteString(), signature.toByteString())

    public companion object : TlCodec<LiteServerSignature> by LiteServerSignatureTlConstructor
}

private object LiteServerSignatureTlConstructor : TlConstructor<LiteServerSignature>(
    schema = "liteServer.signature node_id_short:int256 signature:bytes = liteServer.Signature"
) {
    override fun decode(reader: TlReader): LiteServerSignature {
        val nodeIdShort = reader.readByteString(32)
        val signature = reader.readByteString()
        return LiteServerSignature(nodeIdShort, signature)
    }

    override fun encode(writer: TlWriter, value: LiteServerSignature) {
        writer.writeRaw(value.nodeIdShort)
        writer.writeBytes(value.signature)
    }
}
