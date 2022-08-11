package org.ton.api.pk

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.api.pub.PublicKeyOverlay
import org.ton.crypto.Decryptor
import org.ton.crypto.DecryptorFail
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readBytesTl
import org.ton.tl.constructors.writeBytesTl

@JsonClassDiscriminator("@type")
@SerialName("pk.overlay")
@Serializable
data class PrivateKeyOverlay(
    val name: ByteArray
) : PrivateKey, Decryptor by DecryptorFail {
    override fun publicKey() = PublicKeyOverlay(name)

    override fun toString(): String = toAdnlIdShort().toString()

    companion object : TlConstructor<PrivateKeyOverlay>(
        type = PrivateKeyOverlay::class,
        schema = "pk.overlay name:bytes = PrivateKey"
    ) {
        override fun encode(output: Output, value: PrivateKeyOverlay) {
            output.writeBytesTl(value.name)
        }

        override fun decode(input: Input): PrivateKeyOverlay {
            val name = input.readBytesTl()
            return PrivateKeyOverlay(name)
        }
    }
}
