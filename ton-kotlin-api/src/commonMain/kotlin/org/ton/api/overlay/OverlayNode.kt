package org.ton.api.overlay

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.SignedTlObject
import org.ton.api.pk.PrivateKey
import org.ton.api.pub.PublicKey
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.*
import org.ton.tl.readTl
import org.ton.tl.writeTl

@Serializable
@SerialName("overlay.node")
data class OverlayNode(
    val id: PublicKey,
    val overlay: ByteArray,
    val version: Int,
    override val signature: ByteArray = ByteArray(0)
) : SignedTlObject<OverlayNode> {
    override fun signed(privateKey: PrivateKey): OverlayNode =
        copy(
            signature = privateKey.sign(
                tlCodec().encodeBoxed(
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
        return publicKey.verify(tlCodec().encodeBoxed(check), signature)
    }

    override fun tlCodec(): TlCodec<OverlayNode> = Companion

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is OverlayNode) return false

        if (version != other.version) return false
        if (!signature.contentEquals(other.signature)) return false
        if (id != other.id) return false
        if (!overlay.contentEquals(other.overlay)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + overlay.contentHashCode()
        result = 31 * result + version
        result = 31 * result + signature.contentHashCode()
        return result
    }

    companion object : TlConstructor<OverlayNode>(
        schema = "overlay.node id:PublicKey overlay:int256 version:int signature:bytes = overlay.Node",
    ) {
        override fun encode(output: Output, value: OverlayNode) {
            output.writeTl(PublicKey, value.id)
            output.writeInt256Tl(value.overlay)
            output.writeIntTl(value.version)
            output.writeBytesTl(value.signature)
        }

        override fun decode(input: Input): OverlayNode {
            val id = input.readTl(PublicKey)
            val overlay = input.readInt256Tl()
            val version = input.readIntTl()
            val signature = input.readBytesTl()
            return OverlayNode(id, overlay, version, signature)
        }
    }
}
