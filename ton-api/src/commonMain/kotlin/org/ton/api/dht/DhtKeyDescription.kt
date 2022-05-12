package org.ton.api.dht

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.pub.PublicKey
import org.ton.crypto.Base64ByteArraySerializer
import org.ton.crypto.base64
import org.ton.tl.TlConstructor

@Serializable
data class DhtKeyDescription(
        val key: DhtKey,
        val id: PublicKey,
        @SerialName("update_rule")
        val updateRule: DhtUpdateRule,
        @Serializable(Base64ByteArraySerializer::class)
        val signature: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DhtKeyDescription

        if (key != other.key) return false
        if (id != other.id) return false
        if (updateRule != other.updateRule) return false
        if (!signature.contentEquals(other.signature)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = key.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + updateRule.hashCode()
        result = 31 * result + signature.contentHashCode()
        return result
    }

    override fun toString(): String = buildString {
        append("DhtKeyDescription(key=")
        append(key)
        append(", id=")
        append(id)
        append(", updateRule=")
        append(updateRule)
        append(", signature=")
        append(base64(signature))
        append(")")
    }

    companion object : TlConstructor<DhtKeyDescription>(
            type = DhtKeyDescription::class,
            schema = "dht.keyDescription key:dht.key id:PublicKey update_rule:dht.UpdateRule signature:bytes = dht.KeyDescription"
    ) {
        override fun encode(output: Output, message: DhtKeyDescription) {
            output.writeTl(message.key, DhtKey)
            output.writeTl(message.id, PublicKey)
            output.writeTl(message.updateRule, DhtUpdateRule)
            output.writeByteArray(message.signature)
        }

        override fun decode(input: Input): DhtKeyDescription {
            val key = input.readTl(DhtKey)
            val id = input.readTl(PublicKey)
            val updateRule = input.readTl(DhtUpdateRule)
            val signature = input.readByteArray()
            return DhtKeyDescription(key, id, updateRule, signature)
        }
    }
}