package org.ton.api.pk

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.api.pub.PublicKeyUnencrypted
import org.ton.crypto.Decryptor
import org.ton.crypto.DecryptorNone
import org.ton.tl.TlConstructor
import org.ton.tl.TlReader
import org.ton.tl.TlWriter

@JsonClassDiscriminator("@type")
@SerialName("pk.unenc")
@Serializable
public data class PrivateKeyUnencrypted(
    val data: ByteArray
) : PrivateKey, Decryptor by DecryptorNone {
    override fun publicKey(): PublicKeyUnencrypted = PublicKeyUnencrypted(data)

    override fun hashCode(): Int = data.contentHashCode()

    override fun toString(): String = toAdnlIdShort().toString()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PrivateKeyUnencrypted) return false
        if (!data.contentEquals(other.data)) return false
        return true
    }

    public companion object : TlConstructor<PrivateKeyUnencrypted>(
        schema = "pk.unenc data:bytes = PrivateKey"
    ) {
        override fun encode(writer: TlWriter, value: PrivateKeyUnencrypted) {
            writer.writeBytes(value.data)
        }

        override fun decode(reader: TlReader): PrivateKeyUnencrypted {
            val data = reader.readBytes()
            return PrivateKeyUnencrypted(data)
        }
    }
}
