package org.ton.api.dht

import io.ktor.utils.io.core.*
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import org.ton.api.SignedTlObject
import org.ton.api.pk.PrivateKey
import org.ton.api.pub.PublicKey
import org.ton.crypto.base64.Base64ByteArraySerializer
import org.ton.crypto.base64.base64
import org.ton.tl.TlCodec
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
    override val signature: ByteArray = ByteArray(0)
) : SignedTlObject<DhtValue> {
    constructor(
        key: DhtKeyDescription,
        value: ByteArray,
        ttl: Instant,
        signature: ByteArray
    ) : this(key, value, ttl.epochSeconds.toUInt().toInt(), signature)

    fun ttl(): Instant = Instant.fromEpochSeconds(ttl.toUInt().toLong())

    override fun signed(privateKey: PrivateKey): DhtValue =
        copy(signature = privateKey.sign(tlCodec().encodeBoxed(this)))

    override fun verify(publicKey: PublicKey): Boolean =
        publicKey.verify(tlCodec().encodeBoxed(copy(signature = ByteArray(0))), signature)

    override fun tlCodec(): TlCodec<DhtValue> = DhtValue

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

    companion object : TlCodec<DhtValue> by DhtValueTlConstructor {
        @JvmStatic
        fun signed(name: String, value: ByteArray, key: PrivateKey, ttl: Int = Int.MAX_VALUE): DhtValue {
            val dhtValue = DhtValue(
                key = DhtKeyDescription.signed(name, key),
                ttl = ttl,
                value = value
            )
            return dhtValue.signed(key)
        }
    }
}

private object DhtValueTlConstructor : TlConstructor<DhtValue>(
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
