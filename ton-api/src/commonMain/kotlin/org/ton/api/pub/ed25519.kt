@file:Suppress("NOTHING_TO_INLINE")

package org.ton.api.pub

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.adnl.AdnlAddressList.Companion.writeBoxedTl
import org.ton.api.adnl.AdnlIdShort
import org.ton.api.pk.PrivateKeyEd25519
import org.ton.crypto.Base64ByteArraySerializer
import org.ton.crypto.Encryptor
import org.ton.crypto.ed25519.Ed25519
import org.ton.crypto.ed25519.EncryptorEd25519
import org.ton.crypto.ed25519.KEY_SIZE
import org.ton.crypto.hex
import org.ton.crypto.sha256
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor

inline fun PublicKeyEd25519(privateKey: PrivateKeyEd25519) = PublicKeyEd25519.of(privateKey)
inline fun PublicKeyEd25519(byteArray: ByteArray) = PublicKeyEd25519.of(byteArray)

interface PublicKeyEd25519 : PublicKey, Encryptor {
    val key: ByteArray

    override fun toAdnlIdShort(): AdnlIdShort = AdnlIdShort(
        sha256(
            buildPacket {
                writeBoxedTl(PublicKeyEd25519, this@PublicKeyEd25519)
            }.readBytes()
        )
    )

    companion object : TlCodec<PublicKeyEd25519> by PublicKeyEd25519TlConstructor {
        @JvmStatic
        fun tlConstructor(): TlConstructor<PublicKeyEd25519> = PublicKeyEd25519TlConstructor

        @JvmStatic
        fun of(privateKey: PrivateKeyEd25519): PublicKeyEd25519 = of(Ed25519.publicKey(privateKey.key))

        @JvmStatic
        fun of(byteArray: ByteArray) =
            when (byteArray.size) {
                Ed25519.KEY_SIZE -> PublicKeyEd25519Impl(byteArray.copyOf())
                Ed25519.KEY_SIZE + Int.SIZE_BYTES -> decodeBoxed(byteArray)
                else -> throw IllegalArgumentException("Invalid key size: ${byteArray.size}")
            }
    }
}

@SerialName("pub.ed25519")
@Serializable
private class PublicKeyEd25519Impl(
    @Serializable(Base64ByteArraySerializer::class)
    private val _key: ByteArray
) : PublicKeyEd25519, Encryptor by EncryptorEd25519(_key) {
    override val key: ByteArray
        get() = _key.copyOf()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PublicKeyEd25519) return false
        if (!key.contentEquals(other.key)) return false
        return true
    }

    override fun hashCode(): Int = key.contentHashCode()
    override fun toString(): String = hex(key)
}

private object PublicKeyEd25519TlConstructor : TlConstructor<PublicKeyEd25519>(
    type = PublicKeyEd25519::class,
    schema = "pub.ed25519 key:int256 = PublicKey"
) {
    override fun encode(output: Output, value: PublicKeyEd25519) {
        output.writeFully(value.key)
    }

    override fun decode(input: Input): PublicKeyEd25519 {
        val key = input.readBytes(32)
        return PublicKeyEd25519Impl(key)
    }
}