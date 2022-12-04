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
import kotlin.reflect.typeOf

@JsonClassDiscriminator("@type")
@SerialName("pk.unenc")
@Serializable
data class PrivateKeyUnencrypted(
    val data: ByteArray
) : PrivateKey, Decryptor by DecryptorNone {
    override fun publicKey() = PublicKeyUnencrypted(data)



    override fun hashCode(): Int {
        return data.contentHashCode()
    }

    override fun toString(): String = toAdnlIdShort().toString()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PrivateKeyUnencrypted) return false
        if (!data.contentEquals(other.data)) return false
        return true
    }

    companion object : TlConstructor<PrivateKeyUnencrypted>(
        type = typeOf<PrivateKeyUnencrypted>(),
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
