@file:Suppress("OPT_IN_USAGE")

package org.ton.api.pub

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.api.adnl.AdnlIdShort
import org.ton.api.dht.DhtKeyDescription
import org.ton.api.dht.DhtUpdateRule
import org.ton.crypto.*
import org.ton.crypto.aes.EncryptorAes
import org.ton.tl.TlCodec
import org.ton.tl.TlCombinator
import org.ton.tl.TlConstructor
import org.ton.tl.TlObject
import org.ton.tl.constructors.readBytesTl
import org.ton.tl.constructors.writeBytesTl

@Serializable
@JsonClassDiscriminator("@type")
sealed interface PublicKey : Encryptor, TlObject<PublicKey> {
    override fun tlCodec(): TlCodec<out PublicKey> = Companion

    fun toAdnlIdShort(): AdnlIdShort

    companion object : TlCombinator<PublicKey>(
        PublicKeyEd25519.tlConstructor(),
        PublicKeyUnencrypted,
        PublicKeyAes,
        PublicKeyOverlay
    )
}

@SerialName("pub.unenc")
@Serializable
data class PublicKeyUnencrypted(
    @Serializable(Base64ByteArraySerializer::class)
    val data: ByteArray
) : PublicKey, Encryptor by EncryptorNone {

    override fun toAdnlIdShort() = AdnlIdShort(PublicKeyUnencrypted.hash(this))

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

@SerialName("pub.aes")
@Serializable
data class PublicKeyAes(
    @Serializable(Base64ByteArraySerializer::class)
    val key: ByteArray
) : PublicKey, Encryptor by EncryptorAes(key) {
    private val _adnlIdShort by lazy { AdnlIdShort(hash(this)) }

    override fun toAdnlIdShort() = _adnlIdShort

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
) : PublicKey, Encryptor by EncryptorFail {

    override fun toAdnlIdShort(): AdnlIdShort = AdnlIdShort(
        PublicKeyOverlay.hash(this)
    )

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

    override fun verify(message: ByteArray, signature: ByteArray?): Boolean {
        if (signature == null || signature.isNotEmpty()) return false
        val result = try {
            DhtKeyDescription.decodeBoxed(message)
        } catch (e: Exception) {
            return false
        }
        if (result.update_rule != DhtUpdateRule.OVERLAY_NODES) return false
        if (result.signature.isNotEmpty()) return false
        return true
    }
}
