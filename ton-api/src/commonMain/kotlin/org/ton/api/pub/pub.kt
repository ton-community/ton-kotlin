@file:Suppress("OPT_IN_USAGE")

package org.ton.api.pub

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.crypto.Base64ByteArraySerializer
import org.ton.crypto.Ed25519
import org.ton.crypto.base64
import org.ton.crypto.hex
import org.ton.tl.TlCombinator
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readBytesTl
import org.ton.tl.constructors.writeBytesTl

@JsonClassDiscriminator("@type")
interface PublicKey {
    companion object : TlCombinator<PublicKey>(
            PublicKeyUnencrypted,
            PublicKeyEd25519,
            PublicKeyAes,
            PublicKeyOverlay
    )
}

@SerialName("pub.unenc")
@Serializable
data class PublicKeyUnencrypted(
        @Serializable(Base64ByteArraySerializer::class)
        val data: ByteArray
) : PublicKey {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PublicKeyUnencrypted

        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        return data.contentHashCode()
    }

    override fun toString(): String = buildString {
        append("PublicKeyUnencrypted(data=")
        append(base64(data))
        append(")")
    }

    companion object : TlConstructor<PublicKeyUnencrypted>(
            type = PublicKeyUnencrypted::class,
            schema = "pub.unenc data:bytes = PublicKey"
    ) {
        override fun encode(output: Output, value: PublicKeyUnencrypted) {
            output.writeBytesTl(value.data)
        }

        override fun decode(input: Input): PublicKeyUnencrypted {
            val data = input.readBytesTl()
            return PublicKeyUnencrypted(data)
        }
    }
}

@SerialName("pub.ed25519")
@Serializable
data class PublicKeyEd25519(
        @Serializable(Base64ByteArraySerializer::class)
        val key: ByteArray
) : PublicKey {
    init {
        require(key.size == 32) { "key size expected: 32 actual: ${key.size}" }
    }

    fun verify(signature: ByteArray, byteArray: ByteArray): Boolean =
        Ed25519.verify(signature, key, byteArray)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PublicKeyEd25519

        if (!key.contentEquals(other.key)) return false

        return true
    }

    override fun hashCode(): Int {
        return key.contentHashCode()
    }

    override fun toString(): String = hex(key)

    companion object : TlConstructor<PublicKeyEd25519>(
            type = PublicKeyEd25519::class,
            schema = "pub.ed25519 key:int256 = PublicKey"
    ) {
        override fun encode(output: Output, value: PublicKeyEd25519) {
            output.writeFully(value.key)
        }

        override fun decode(input: Input): PublicKeyEd25519 {
            val key = input.readBytes(32)
            return PublicKeyEd25519(key)
        }
    }
}

@SerialName("pub.aes")
@Serializable
data class PublicKeyAes(
        @Serializable(Base64ByteArraySerializer::class)
        val key: ByteArray
) : PublicKey {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PublicKeyAes

        if (!key.contentEquals(other.key)) return false

        return true
    }

    override fun hashCode(): Int {
        return key.contentHashCode()
    }

    override fun toString(): String = buildString {
        append("PublicKeyAes(key=")
        append(base64(key))
        append(")")
    }

    companion object : TlConstructor<PublicKeyAes>(
            type = PublicKeyAes::class,
            schema = "pub.aes key:int256 = PublicKey"
    ) {
        override fun encode(output: Output, value: PublicKeyAes) {
            output.writeFully(value.key)
        }

        override fun decode(input: Input): PublicKeyAes {
            val key = input.readBytes(32)
            return PublicKeyAes(key)
        }
    }
}

@SerialName("pub.overlay")
@Serializable
data class PublicKeyOverlay(
        val name: String
) : PublicKey {
    companion object : TlConstructor<PublicKeyOverlay>(
            type = PublicKeyOverlay::class,
            schema = "pub.overlay name:bytes = PublicKey"
    ) {
        override fun encode(output: Output, value: PublicKeyOverlay) {
            output.writeBytesTl(value.name.encodeToByteArray())
        }

        override fun decode(input: Input): PublicKeyOverlay {
            val name = input.readBytesTl().decodeToString()
            return PublicKeyOverlay(name)
        }
    }
}
