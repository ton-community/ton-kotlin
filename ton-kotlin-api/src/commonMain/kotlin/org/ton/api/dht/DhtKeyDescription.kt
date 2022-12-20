package org.ton.api.dht

import kotlinx.serialization.Serializable
import org.ton.api.SignedTlObject
import org.ton.api.pk.PrivateKey
import org.ton.api.pub.PublicKey
import org.ton.crypto.base64
import org.ton.tl.*
import kotlin.jvm.JvmStatic

@Serializable
public data class DhtKeyDescription(
    val key: DhtKey,
    val id: PublicKey,
    val update_rule: DhtUpdateRule = DhtUpdateRule.SIGNATURE,
    override val signature: ByteArray = ByteArray(0)
) : SignedTlObject<DhtKeyDescription> {
    override fun signed(privateKey: PrivateKey): DhtKeyDescription =
        copy(
            signature = privateKey.sign(
                copy(signature = ByteArray(0)).toByteArray()
            )
        )

    override fun verify(publicKey: PublicKey): Boolean =
        publicKey.verify(tlCodec().encodeToByteArray(copy(signature = ByteArray(0))), signature)

    override fun tlCodec(): TlCodec<DhtKeyDescription> = DhtKeyDescriptionTlConstructor

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DhtKeyDescription) return false
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

    public companion object : TlCodec<DhtKeyDescription> by DhtKeyDescriptionTlConstructor {
        @JvmStatic
        public fun signed(name: String, key: PrivateKey): DhtKeyDescription {
            val keyDescription = DhtKeyDescription(
                id = key.publicKey(),
                key = DhtKey(key.publicKey().toAdnlIdShort(), name)
            )
            return keyDescription.signed(key)
        }
    }
}

private object DhtKeyDescriptionTlConstructor : TlConstructor<DhtKeyDescription>(
    schema = "dht.keyDescription key:dht.key id:PublicKey update_rule:dht.UpdateRule signature:bytes = dht.KeyDescription"
) {
    override fun encode(writer: TlWriter, value: DhtKeyDescription) {
        writer.write(DhtKey, value.key)
        writer.write(PublicKey, value.id)
        writer.write(DhtUpdateRule, value.update_rule)
        writer.writeBytes(value.signature)
    }

    override fun decode(reader: TlReader): DhtKeyDescription {
        val key = reader.read(DhtKey)
        val id = reader.read(PublicKey)
        val updateRule = reader.read(DhtUpdateRule)
        val signature = reader.readBytes()
        return DhtKeyDescription(key, id, updateRule, signature)
    }
}
