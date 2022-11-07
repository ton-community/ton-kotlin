package org.ton.api.pk

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.api.pub.PublicKeyAes
import org.ton.crypto.Decryptor
import org.ton.crypto.aes.DecryptorAes
import org.ton.tl.TlConstructor

@JsonClassDiscriminator("@type")
@SerialName("pk.aes")
@Serializable
data class PrivateKeyAes(
    val key: ByteArray
) : PrivateKey, Decryptor by DecryptorAes(key) {
    private val _hashCode by lazy { key.contentHashCode() }

    override fun publicKey() = PublicKeyAes(key)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PrivateKeyAes) return false
        if (!key.contentEquals(other.key)) return false
        return true
    }

    override fun hashCode(): Int = _hashCode
    override fun toString(): String = toAdnlIdShort().toString()

    companion object : TlConstructor<PrivateKeyAes>(
        type = PrivateKeyAes::class,
        schema = "pk.aes key:int256 = PrivateKey"
    ) {
        override fun encode(output: Output, value: PrivateKeyAes) {
            output.writeFully(value.key)
        }

        override fun decode(input: Input): PrivateKeyAes {
            val key = input.readBytes(32)
            return PrivateKeyAes(key)
        }
    }
}
