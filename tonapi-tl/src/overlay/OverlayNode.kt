package org.ton.api.overlay

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.SignedTlObject
import org.ton.api.pk.PrivateKey
import org.ton.api.pub.PublicKey
import org.ton.tl.*
import org.ton.tl.ByteString.Companion.toByteString

@Serializable
@SerialName("overlay.node")
public data class OverlayNode(
    val id: PublicKey,
    val overlay: ByteString,
    val version: Int,
    override val signature: ByteString = ByteString.of()
) : SignedTlObject<OverlayNode> {
    public constructor(
        id: PublicKey,
        overlay: ByteArray,
        version: Int,
        signature: ByteArray = ByteArray(0)
    ) : this(
        id, overlay.toByteString(), version, signature.toByteString()
    )

    override fun signed(privateKey: PrivateKey): OverlayNode =
        copy(
            signature = ByteString.of(
                *privateKey.sign(
                    tlCodec().encodeToByteArray(
                        if (signature.isEmpty()) this
                        else copy(signature = ByteString.of())
                    )
                )
            )
        )

    override fun verify(publicKey: PublicKey): Boolean {
        if (signature.isEmpty()) return false
        val check = copy(
            signature = ByteString.of()
        )
        return publicKey.verify(tlCodec().encodeToByteArray(check), signature.toByteArray())
    }

    override fun tlCodec(): TlCodec<OverlayNode> = Companion

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
            val overlay = reader.readByteString(32)
            val version = reader.readInt()
            val signature = reader.readByteString()
            return OverlayNode(id, overlay, version, signature)
        }
    }
}
