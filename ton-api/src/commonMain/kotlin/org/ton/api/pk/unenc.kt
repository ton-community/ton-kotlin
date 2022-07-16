package org.ton.api.pk

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.api.pub.PublicKeyUnencrypted
import org.ton.crypto.Decryptor
import org.ton.crypto.DecryptorNone
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readBytesTl
import org.ton.tl.constructors.writeBytesTl

@JsonClassDiscriminator("@type")
@SerialName("pk.unenc")
@Serializable
data class PrivateKeyUnencrypted(
    val data: ByteArray
) : PrivateKey, Decryptor by DecryptorNone {
    override fun publicKey() = PublicKeyUnencrypted(data)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PrivateKeyUnencrypted

        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        return data.contentHashCode()
    }

    override fun toString(): String = toAdnlIdShort().toString()

    companion object : TlConstructor<PrivateKeyUnencrypted>(
        type = PrivateKeyUnencrypted::class,
        schema = "pk.unenc data:bytes = PrivateKey"
    ) {
        override fun encode(output: Output, value: PrivateKeyUnencrypted) {
            output.writeBytesTl(value.data)
        }

        override fun decode(input: Input): PrivateKeyUnencrypted {
            val data = input.readBytesTl()
            return PrivateKeyUnencrypted(data)
        }
    }
}
