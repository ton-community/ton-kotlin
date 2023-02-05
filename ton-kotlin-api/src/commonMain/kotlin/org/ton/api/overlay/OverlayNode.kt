package org.ton.api.overlay

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.SignedTlObject
import org.ton.api.pk.PrivateKey
import org.ton.api.pub.PublicKey
import org.ton.tl.*
import org.ton.tl.constructors.*

@Serializable
@SerialName("overlay.node")
public data class OverlayNode(
    val id: PublicKey,
    val overlay: ByteArray,
    val version: Int,
    override val signature: ByteArray = ByteArray(0)
) : SignedTlObject<OverlayNode> {
    override fun signed(privateKey: PrivateKey): OverlayNode =
        copy(
            signature = privateKey.sign(
                tlCodec().encodeToByteArray(
                    if (signature.isEmpty()) this
                    else copy(signature = ByteArray(0))
                )
            )
        )

    override fun verify(publicKey: PublicKey): Boolean {
        if (signature.isEmpty()) return false
        val check = copy(
            signature = ByteArray(0)
        )
        return publicKey.verify(tlCodec().encodeToByteArray(check), signature)
    }

    override fun tlCodec(): TlCodec<OverlayNode> = Companion

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is OverlayNode) return false

        if (id != other.id) return false
        if (!overlay.contentEquals(other.overlay)) return false
        if (version != other.version) return false
        if (!signature.contentEquals(other.signature)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + overlay.hashCode()
        result = 31 * result + version
        result = 31 * result + signature.contentHashCode()
        return result
    }

    public companion object : TlConstructor<OverlayNode>(
        schema = "overlay.node id:PublicKey overlay:int256 version:int signature:bytes = overlay.Node",
    ) {
        override fun encode(writer: TlWriter, value: OverlayNode) {
            writer.write(PublicKey, value.id)
            writer.writeRaw(value.overlay)
            writer.writeInt(value.version)
            writer.writeBytes(value.signature)
        }

        override fun decode(reader: TlReader): OverlayNode {
            val id = reader.read(PublicKey)
            val overlay = reader.readRaw(32)
            val version = reader.readInt()
            val signature = reader.readBytes()
            return OverlayNode(id, overlay, version, signature)
        }
    }
}
