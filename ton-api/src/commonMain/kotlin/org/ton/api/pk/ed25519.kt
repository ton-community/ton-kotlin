@file:Suppress("NOTHING_TO_INLINE", "OPT_IN_USAGE")

package org.ton.api.pk

import io.ktor.utils.io.core.*
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.api.pub.PublicKeyEd25519
import org.ton.crypto.Decryptor
import org.ton.crypto.SecureRandom
import org.ton.crypto.X25519
import org.ton.crypto.ed25519.DecryptorEd25519
import org.ton.crypto.ed25519.Ed25519
import org.ton.crypto.ed25519.KEY_SIZE
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import kotlin.random.Random

inline fun PrivateKeyEd25519(byteArray: ByteArray) = PrivateKeyEd25519.of(byteArray)
inline fun PrivateKeyEd25519(random: Random = SecureRandom) = PrivateKeyEd25519.generate(random)

interface PrivateKeyEd25519 : PrivateKey {
    val key: ByteArray

    override fun publicKey() =
        PublicKeyEd25519(Ed25519.publicKey(key))

    fun sharedSecret(publicKey: PublicKeyEd25519) =
        X25519.sharedKey(publicKey.key, Ed25519.convertToX25519(publicKey.key))

    companion object : TlCodec<PrivateKeyEd25519> by PrivateKeyEd25519TlConstructor {
        const val KEY_SIZE = 32

        @JvmStatic
        fun tlConstructor(): TlConstructor<PrivateKeyEd25519> = PrivateKeyEd25519TlConstructor

        @JvmStatic
        fun generate(random: Random = SecureRandom): PrivateKeyEd25519 =
            PrivateKeyEd25519Impl(random.nextBytes(KEY_SIZE))

        @JvmStatic
        fun of(byteArray: ByteArray): PrivateKeyEd25519 =
            when (byteArray.size) {
                Ed25519.KEY_SIZE -> PrivateKeyEd25519Impl(byteArray.copyOf())
                Ed25519.KEY_SIZE + Int.SIZE_BYTES -> decodeBoxed(byteArray)
                else -> throw IllegalArgumentException("Invalid key size: ${byteArray.size}")
            }
    }
}

@Polymorphic
@JsonClassDiscriminator("@type")
@SerialName("pk.ed25519")
@Serializable
private class PrivateKeyEd25519Impl(
    private val _key: ByteArray
) : PrivateKeyEd25519, Decryptor by DecryptorEd25519(_key) {
    init {
        require(_key.size == PrivateKeyEd25519.KEY_SIZE) { "key size expected: 32 actual: ${_key.size}" }
    }

    private val _publicKey: PublicKeyEd25519 by lazy { super.publicKey() }

    override val key: ByteArray get() = _key.copyOf()

    override fun publicKey(): PublicKeyEd25519 = _publicKey

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PrivateKeyEd25519) return false
        if (!_key.contentEquals(other.key)) return false
        return true
    }

    override fun hashCode(): Int = _key.contentHashCode()

    override fun toString(): String = toAdnlIdShort().toString()
}

private object PrivateKeyEd25519TlConstructor : TlConstructor<PrivateKeyEd25519>(
    type = PrivateKeyEd25519::class,
    schema = "pk.ed25519 key:int256 = PrivateKey"
) {
    override fun encode(output: Output, value: PrivateKeyEd25519) {
        output.writeFully(value.key)
    }

    override fun decode(input: Input): PrivateKeyEd25519 {
        val key = input.readBytes(32)
        return PrivateKeyEd25519Impl(key)
    }
}