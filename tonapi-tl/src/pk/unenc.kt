package org.ton.api.pk

import kotlinx.io.bytestring.ByteString
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.api.pub.PublicKeyUnencrypted
import org.ton.crypto.Decryptor
import org.ton.crypto.DecryptorNone
import org.ton.tl.ByteStringBase64Serializer
import org.ton.tl.TlConstructor
import org.ton.tl.TlReader
import org.ton.tl.TlWriter

@JsonClassDiscriminator("@type")
@SerialName("pk.unenc")
@Serializable
public data class PrivateKeyUnencrypted(
    @Serializable(ByteStringBase64Serializer::class)
    val data: ByteString
) : PrivateKey, Decryptor by DecryptorNone {
    override fun publicKey(): PublicKeyUnencrypted = PublicKeyUnencrypted(data)

    override fun toString(): String = toAdnlIdShort().toString()

    public companion object : TlConstructor<PrivateKeyUnencrypted>(
        schema = "pk.unenc data:bytes = PrivateKey"
    ) {
        override fun encode(writer: TlWriter, value: PrivateKeyUnencrypted) {
            writer.writeBytes(value.data)
        }

        override fun decode(reader: TlReader): PrivateKeyUnencrypted {
            return PrivateKeyUnencrypted(reader.readByteString())
        }
    }
}
