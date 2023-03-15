package org.ton.api.pk

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.api.pub.PublicKeyOverlay
import org.ton.crypto.Decryptor
import org.ton.crypto.DecryptorFail
import org.ton.tl.TlConstructor
import org.ton.tl.TlReader
import org.ton.tl.TlWriter

@JsonClassDiscriminator("@type")
@SerialName("pk.overlay")
@Serializable
public data class PrivateKeyOverlay(
    val name: ByteArray
) : PrivateKey, Decryptor by DecryptorFail {
    override fun publicKey(): PublicKeyOverlay = PublicKeyOverlay(name)

    override fun toString(): String = toAdnlIdShort().toString()

    public companion object : TlConstructor<PrivateKeyOverlay>(
        schema = "pk.overlay name:bytes = PrivateKey"
    ) {
        override fun encode(writer: TlWriter, value: PrivateKeyOverlay) {
            writer.writeBytes(value.name)
        }

        override fun decode(input: TlReader): PrivateKeyOverlay {
            val name = input.readBytes()
            return PrivateKeyOverlay(name)
        }
    }
}
