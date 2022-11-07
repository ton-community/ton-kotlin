@file:Suppress("NOTHING_TO_INLINE")

package org.ton.api.pub

import io.ktor.utils.io.core.*
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.adnl.AdnlIdShort
import org.ton.api.pk.PrivateKeyEd25519
import org.ton.crypto.Base64ByteArraySerializer
import org.ton.crypto.Encryptor
import org.ton.crypto.ed25519.Ed25519
import org.ton.crypto.ed25519.EncryptorEd25519
import org.ton.crypto.hex
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.Int256TlConstructor

inline fun PublicKeyEd25519(privateKey: PrivateKeyEd25519) = PublicKeyEd25519.of(privateKey)

@Serializable
@SerialName("pub.ed25519")
@Polymorphic
data class PublicKeyEd25519(
    @Serializable(Base64ByteArraySerializer::class)
    val key: ByteArray
) : PublicKey, Encryptor by EncryptorEd25519(key) {
    private val _adnlIdShort: AdnlIdShort by lazy { AdnlIdShort(PublicKeyEd25519.hash(this)) }

    override fun toAdnlIdShort(): AdnlIdShort = _adnlIdShort

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PublicKeyEd25519) return false
        if (!key.contentEquals(other.key)) return false
        return true
    }

    override fun hashCode(): Int = key.contentHashCode()
    override fun toString(): String = hex(key)

    companion object : TlCodec<PublicKeyEd25519> by PublicKeyEd25519TlConstructor {
        @JvmStatic
        fun tlConstructor(): TlConstructor<PublicKeyEd25519> = PublicKeyEd25519TlConstructor

        @JvmStatic
        fun of(privateKey: PrivateKeyEd25519): PublicKeyEd25519 =
            of(Ed25519.publicKey(privateKey.key))

        @JvmStatic
        fun of(byteArray: ByteArray) =
            when (byteArray.size) {
                Ed25519.KEY_SIZE_BYTES -> PublicKeyEd25519(byteArray.copyOf())
                Ed25519.KEY_SIZE_BYTES + Int.SIZE_BYTES -> decodeBoxed(byteArray)
                else -> throw IllegalArgumentException("Invalid key size: ${byteArray.size}")
            }
    }
}

private object PublicKeyEd25519TlConstructor : TlConstructor<PublicKeyEd25519>(
    type = PublicKeyEd25519::class,
    schema = "pub.ed25519 key:int256 = PublicKey",
    fields = listOf(Int256TlConstructor)
) {
    override fun encode(output: Output, value: PublicKeyEd25519) {
        output.writeFully(value.key)
    }

    override fun decode(input: Input): PublicKeyEd25519 {
        val key = input.readBytes(32)
        return PublicKeyEd25519(key)
    }

    override fun decode(values: Iterator<*>): PublicKeyEd25519 {
        val key = values.next() as ByteArray
        return PublicKeyEd25519(key)
    }
}
