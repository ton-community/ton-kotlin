@file:Suppress("OPT_IN_USAGE")

package org.ton.api.pub

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.api.adnl.AdnlAddressList.Companion.writeBoxedTl
import org.ton.api.adnl.AdnlIdShort
import org.ton.crypto.*
import org.ton.tl.TlCodec
import org.ton.tl.TlCombinator
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readBytesTl
import org.ton.tl.constructors.writeBytesTl
import org.ton.tl.writeTl
import kotlin.experimental.xor

@JsonClassDiscriminator("@type")
interface PublicKey {
    infix fun affinity(key: PublicKey): Int
    fun toByteArray(): ByteArray
    fun toAdnlIdShort(): AdnlIdShort

    companion object : TlCombinator<PublicKey>(
        PublicKeyUnencrypted,
        PublicKeyEd25519.tlConstructor(),
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

    override fun toByteArray(): ByteArray = data.copyOf()

    override fun affinity(key: PublicKey): Int {
        TODO("Not yet implemented")
    }

    override fun toAdnlIdShort(): AdnlIdShort = AdnlIdShort(
        sha256(
            buildPacket {
                writeTl(PublicKeyUnencrypted, this@PublicKeyUnencrypted)
            }.readBytes()
        )
    )

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

    override fun toByteArray(): ByteArray = key.copyOf()

    override fun toAdnlIdShort(): AdnlIdShort = AdnlIdShort(
        sha256(
            buildPacket {
                writeBoxedTl(PublicKeyEd25519, this@PublicKeyEd25519)
            }.readBytes()
        )
    )

    fun verify(signature: ByteArray, byteArray: ByteArray): Boolean =
        Ed25519.verify(signature, key, byteArray)

    override fun affinity(key: PublicKey): Int {
        return if (key is PublicKeyEd25519) {
            Companion.affinity(this, key)
        } else {
            TODO()
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PublicKeyEd25519) return false
        if (!key.contentEquals(other.key)) return false
        return true
    }

    override fun hashCode(): Int {
        return key.contentHashCode()
    }

    override fun toString(): String = hex(key)

    companion object : TlCodec<PublicKeyEd25519> by PublicKeyEd25519TlConstructor {
        val BITS = byteArrayOf(4, 3, 2, 2, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0)

        fun tlConstructor(): TlConstructor<PublicKeyEd25519> = PublicKeyEd25519TlConstructor

        fun affinity(key1: PublicKeyEd25519, key2: PublicKeyEd25519): Int {
            var result = 0
            for (i in 0..32) {
                val x = (key1.key[i] xor key2.key[i]).toInt()
                if (x == 0) {
                    result += 8
                } else {
                    if (x and 0xF0 == 0) {
                        result += BITS[x and 0x0F] + 4
                    } else {
                        result += BITS[x shr 4]
                    }
                    break
                }
            }
            return result
        }
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

    override fun toByteArray(): ByteArray = key.copyOf()

    override fun affinity(key: PublicKey): Int {
        TODO("Not yet implemented")
    }

    override fun toAdnlIdShort(): AdnlIdShort = AdnlIdShort(
        sha256(
            buildPacket {
                writeBoxedTl(PublicKeyAes, this@PublicKeyAes)
            }.readBytes()
        )
    )

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
    val name: ByteArray
) : PublicKey {
    override fun toByteArray(): ByteArray = name.copyOf()

    override fun toAdnlIdShort(): AdnlIdShort = AdnlIdShort(
        sha256(
            buildPacket {
                writeBoxedTl(PublicKeyOverlay, this@PublicKeyOverlay)
            }.readBytes()
        )
    )

    override fun affinity(key: PublicKey): Int {
        TODO("Not yet implemented")
    }

    companion object : TlConstructor<PublicKeyOverlay>(
        type = PublicKeyOverlay::class,
        schema = "pub.overlay name:bytes = PublicKey"
    ) {
        override fun encode(output: Output, value: PublicKeyOverlay) {
            output.writeBytesTl(value.name)
        }

        override fun decode(input: Input): PublicKeyOverlay {
            val name = input.readBytesTl()
            return PublicKeyOverlay(name)
        }
    }
}
