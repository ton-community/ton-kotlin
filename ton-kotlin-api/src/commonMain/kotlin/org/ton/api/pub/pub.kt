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
import org.ton.tl.*

@Serializable
@JsonClassDiscriminator("@type")
public sealed interface PublicKey : Encryptor, TlObject<PublicKey> {
    override fun tlCodec(): TlCodec<out PublicKey> = Companion

    public fun toAdnlIdShort(): AdnlIdShort

    public companion object : TlCombinator<PublicKey>(
        PublicKey::class,
        PublicKeyEd25519::class to PublicKeyEd25519.tlConstructor(),
        PublicKeyUnencrypted::class to PublicKeyUnencrypted,
        PublicKeyAes::class to PublicKeyAes,
        PublicKeyOverlay::class to PublicKeyOverlay,
    )
}

@SerialName("pub.unenc")
@Serializable
public data class PublicKeyUnencrypted(
    @Serializable(Base64ByteArraySerializer::class)
    val data: ByteArray
) : PublicKey, Encryptor by EncryptorNone {

    override fun toAdnlIdShort(): AdnlIdShort = AdnlIdShort(PublicKeyUnencrypted.hash(this))

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PublicKeyUnencrypted) return false
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

    public companion object : TlConstructor<PublicKeyUnencrypted>(
        schema = "pub.unenc data:bytes = PublicKey"
    ) {
        override fun encode(output: TlWriter, value: PublicKeyUnencrypted) {
            output.writeBytes(value.data)
        }

        override fun decode(input: TlReader): PublicKeyUnencrypted {
            val data = input.readBytes()
            return PublicKeyUnencrypted(data)
        }
    }
}

@SerialName("pub.aes")
@Serializable
public data class PublicKeyAes(
    @Serializable(Base64ByteArraySerializer::class)
    val key: Bits256
) : PublicKey, Encryptor by EncryptorAes(key.toByteArray()) {
    private val _adnlIdShort by lazy(LazyThreadSafetyMode.PUBLICATION) {
        AdnlIdShort(hash(this))
    }

    override fun toAdnlIdShort(): AdnlIdShort = _adnlIdShort

    public companion object : TlConstructor<PublicKeyAes>(
        schema = "pub.aes key:int256 = PublicKey"
    ) {
        override fun encode(writer: TlWriter, value: PublicKeyAes) {
            writer.writeBits256(value.key)
        }

        override fun decode(reader: TlReader): PublicKeyAes {
            val key = reader.readBits256()
            return PublicKeyAes(key)
        }
    }
}

@SerialName("pub.overlay")
@Serializable
public data class PublicKeyOverlay(
    val name: ByteArray
) : PublicKey, Encryptor by EncryptorFail {

    override fun toAdnlIdShort(): AdnlIdShort = AdnlIdShort(
        PublicKeyOverlay.hash(this)
    )

    override fun verify(message: ByteArray, signature: ByteArray?): Boolean {
        if (signature == null || signature.isNotEmpty()) return false
        val result = try {
            DhtKeyDescription.decodeBoxed(message)
        } catch (e: Exception) {
            return false
        }
        if (result.updateRule != DhtUpdateRule.OVERLAY_NODES) return false
        if (result.signature.isNotEmpty()) return false
        return true
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PublicKeyOverlay) return false
        if (!name.contentEquals(other.name)) return false
        return true
    }

    override fun hashCode(): Int = name.contentHashCode()

    public companion object : TlConstructor<PublicKeyOverlay>(
        schema = "pub.overlay name:bytes = PublicKey"
    ) {
        override fun encode(output: TlWriter, value: PublicKeyOverlay) {
            output.writeBytes(value.name)
        }

        override fun decode(input: TlReader): PublicKeyOverlay {
            val name = input.readBytes()
            return PublicKeyOverlay(name)
        }
    }
}
