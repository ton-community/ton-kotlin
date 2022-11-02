package org.ton.api.dht

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.api.SignedTlObject
import org.ton.api.pk.PrivateKey
import org.ton.api.pub.PublicKey
import org.ton.crypto.Base64ByteArraySerializer
import org.ton.crypto.base64
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readBytesTl
import org.ton.tl.constructors.readIntTl
import org.ton.tl.constructors.writeBytesTl
import org.ton.tl.readTl
import org.ton.tl.writeTl

@Serializable
data class DhtKeyDescription(
    val key: DhtKey,
    val id: PublicKey,
    val update_rule: DhtUpdateRule = DhtUpdateRule.SIGNATURE,
    @Serializable(Base64ByteArraySerializer::class)
    override val signature: ByteArray = ByteArray(0)
) : SignedTlObject<DhtKeyDescription> {
    override fun signed(privateKey: PrivateKey) =
        copy(signature = privateKey.sign(tlCodec().encodeBoxed(this)))

    override fun verify(publicKey: PublicKey): Boolean =
        publicKey.verify(tlCodec().encodeBoxed(copy(signature = ByteArray(0))), signature)

    override fun tlCodec(): TlCodec<DhtKeyDescription> = DhtKeyDescriptionTlConstructor

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DhtKeyDescription

        if (key != other.key) return false
        if (id != other.id) return false
        if (update_rule != other.update_rule) return false
        if (!signature.contentEquals(other.signature)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = key.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + update_rule.hashCode()
        result = 31 * result + signature.contentHashCode()
        return result
    }

    override fun toString(): String = buildString {
        append("DhtKeyDescription(key=")
        append(key)
        append(", id=")
        append(id)
        append(", updateRule=")
        append(update_rule)
        append(", signature=")
        append(base64(signature))
        append(")")
    }

    companion object : TlCodec<DhtKeyDescription> by DhtKeyDescriptionTlConstructor {
        @JvmStatic
        fun signed(name: String, key: PrivateKey): DhtKeyDescription {
            val keyDescription = DhtKeyDescription(
                id = key.publicKey(),
                key = DhtKey(key.publicKey().toAdnlIdShort(), name)
            )
            return keyDescription.signed(key)
        }
    }
}

private object DhtKeyDescriptionTlConstructor : TlConstructor<DhtKeyDescription>(
    type = DhtKeyDescription::class,
    schema = "dht.keyDescription key:dht.key id:PublicKey update_rule:dht.UpdateRule signature:bytes = dht.KeyDescription"
) {
    override fun encode(output: Output, value: DhtKeyDescription) {
        output.writeTl(value.key)
        output.writeTl(value.id)
        output.writeTl(value.update_rule)
        output.writeBytesTl(value.signature)
    }

    override fun decode(input: Input): DhtKeyDescription {
        val key = input.readTl(DhtKey)
        val id = input.readTl(PublicKey)
        val updateRule = input.readTl(DhtUpdateRule)
        val signature = input.readBytesTl()
        return DhtKeyDescription(key, id, updateRule, signature)
    }
}
