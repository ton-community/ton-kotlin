package org.ton.api.pk

import kotlinx.io.bytestring.ByteString
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.api.pub.PublicKeyOverlay
import org.ton.crypto.Decryptor
import org.ton.crypto.DecryptorFail
import org.ton.tl.ByteStringBase64Serializer
import org.ton.tl.TlConstructor
import org.ton.tl.TlReader
import org.ton.tl.TlWriter

@JsonClassDiscriminator("@type")
@SerialName("pk.overlay")
@Serializable
public data class PrivateKeyOverlay(
    @Serializable(ByteStringBase64Serializer::class)
    val name: ByteString
) : PrivateKey, Decryptor by DecryptorFail {
    override fun publicKey(): PublicKeyOverlay = PublicKeyOverlay(name)

    override fun toString(): String = toAdnlIdShort().toString()

    public companion object : TlConstructor<PrivateKeyOverlay>(
        schema = "pk.overlay name:bytes = PrivateKey"
    ) {
        override fun encode(writer: TlWriter, value: PrivateKeyOverlay) {
            writer.writeBytes(value.name)
        }

        override fun decode(reader: TlReader): PrivateKeyOverlay {
            val name = reader.readByteString()
            return PrivateKeyOverlay(name)
        }
    }
}
