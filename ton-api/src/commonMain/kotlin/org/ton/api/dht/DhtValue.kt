package org.ton.api.dht

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.crypto.Base64ByteArraySerializer
import org.ton.crypto.base64
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readBytesTl
import org.ton.tl.constructors.readIntTl
import org.ton.tl.constructors.writeBytesTl
import org.ton.tl.constructors.writeIntTl
import org.ton.tl.readTl
import org.ton.tl.writeTl

@Serializable
data class DhtValue(
        val key: DhtKeyDescription,
        @Serializable(Base64ByteArraySerializer::class)
        val value: ByteArray,
        val ttl: Int,
        @Serializable(Base64ByteArraySerializer::class)
        val signature: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DhtValue

        if (key != other.key) return false
        if (!value.contentEquals(other.value)) return false
        if (ttl != other.ttl) return false
        if (!signature.contentEquals(other.signature)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = key.hashCode()
        result = 31 * result + value.contentHashCode()
        result = 31 * result + ttl
        result = 31 * result + signature.contentHashCode()
        return result
    }

    override fun toString(): String = buildString {
        append("DhtValue(key=")
        append(key)
        append(", value=")
        append(base64(value))
        append(", ttl=")
        append(ttl)
        append(", signature=")
        append(base64(signature))
        append(")")
    }

    companion object : TlConstructor<DhtValue>(
            type = DhtValue::class,
            schema = "dht.value key:dht.keyDescription value:bytes ttl:int signature:bytes = dht.Value"
    ) {
        override fun encode(output: Output, value: DhtValue) {
            output.writeTl(DhtKeyDescription, value.key)
            output.writeBytesTl(value.value)
            output.writeIntTl(value.ttl)
            output.writeBytesTl(value.signature)
        }

        override fun decode(input: Input): DhtValue {
            val key = input.readTl(DhtKeyDescription)
            val value = input.readBytesTl()
            val ttl = input.readIntTl()
            val signature = input.readBytesTl()
            return DhtValue(key, value, ttl, signature)
        }
    }
}
