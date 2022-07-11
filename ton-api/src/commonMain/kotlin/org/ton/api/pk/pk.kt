@file:Suppress("OPT_IN_USAGE")

package org.ton.api.pk

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.api.pub.*
import org.ton.crypto.Ed25519
import org.ton.crypto.X25519
import org.ton.tl.TlCombinator
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readBytesTl
import org.ton.tl.constructors.writeBytesTl

@JsonClassDiscriminator("@type")
interface PrivateKey {
    fun publicKey(): PublicKey
    fun toByteArray(): ByteArray

    companion object : TlCombinator<PrivateKey>(
        PrivateKeyUnencrypted,
        PrivateKeyEd25519,
        PrivateKeyAes,
        PrivateKeyOverlay
    )

    fun sign(byteArray: ByteArray): ByteArray
}

@SerialName("pk.unenc")
@Serializable
data class PrivateKeyUnencrypted(
    val data: ByteArray
) : PrivateKey {
    override fun toByteArray(): ByteArray = data.copyOf()
    override fun publicKey() = PublicKeyUnencrypted(data)
    override fun sign(byteArray: ByteArray): ByteArray = throw UnsupportedOperationException()

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

@SerialName("pk.ed25519")
@Serializable
data class PrivateKeyEd25519(
    val key: ByteArray
) : PrivateKey {
    init {
        require(key.size == 32) { "key size expected: 32 actual: ${key.size}" }
    }

    override fun toByteArray(): ByteArray = key.copyOf()

    override fun publicKey() = PublicKeyEd25519(
        Ed25519.publicKey(key)
    )

    override fun sign(byteArray: ByteArray): ByteArray =
        Ed25519.sign(key, byteArray)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PrivateKeyEd25519

        if (!key.contentEquals(other.key)) return false

        return true
    }

    override fun hashCode(): Int {
        return key.contentHashCode()
    }

    override fun toString(): String = "pk.ed25519"

    companion object : TlConstructor<PrivateKeyEd25519>(
        type = PrivateKeyEd25519::class,
        schema = "pk.ed25519 key:int256 = PrivateKey"
    ) {
        override fun encode(output: Output, value: PrivateKeyEd25519) {
            output.writeFully(value.key)
        }

        override fun decode(input: Input): PrivateKeyEd25519 {
            val key = input.readBytes(32)
            return PrivateKeyEd25519(key)
        }
    }
}

@SerialName("pk.aes")
@Serializable
data class PrivateKeyAes(
    val key: ByteArray
) : PrivateKey {
    override fun toByteArray(): ByteArray = key.copyOf()
    override fun publicKey() = PublicKeyAes(
        X25519.convertToEd25519(X25519.publicKey(key))
    )

    fun sharedSecret(publicKey: PublicKey): ByteArray {
        return when (publicKey) {
            is PublicKeyAes -> X25519.sharedKey(key, publicKey.key)
            is PublicKeyEd25519 -> X25519.sharedKey(key, Ed25519.convertToX25519(publicKey.key))
            else -> throw UnsupportedOperationException()
        }
    }

    override fun sign(byteArray: ByteArray): ByteArray = throw UnsupportedOperationException()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PrivateKeyAes

        if (!key.contentEquals(other.key)) return false

        return true
    }

    override fun hashCode(): Int {
        return key.contentHashCode()
    }

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

@SerialName("pk.overlay")
@Serializable
data class PrivateKeyOverlay(
    val name: ByteArray
) : PrivateKey {
    override fun toByteArray(): ByteArray = name.copyOf()
    override fun publicKey(): PublicKeyOverlay = PublicKeyOverlay(name)
    override fun sign(byteArray: ByteArray): ByteArray = throw UnsupportedOperationException()

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
