package org.ton.api.dht

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import org.ton.api.SignedTlObject
import org.ton.api.internal.InstantIntSerializer
import org.ton.api.pk.PrivateKey
import org.ton.api.pub.PublicKey
import org.ton.crypto.base64
import org.ton.tl.*

@Serializable
public data class DhtValue(
    val key: DhtKeyDescription,
    val value: ByteArray,
    @Serializable(with = InstantIntSerializer::class)
    val ttl: Instant,
    override val signature: ByteArray = ByteArray(0)
) : SignedTlObject<DhtValue> {
    public constructor(
        key: DhtKeyDescription,
        value: ByteArray,
        ttl: Int,
        signature: ByteArray
    ) : this(key, value, Instant.fromEpochSeconds(ttl.toUInt().toLong()), signature)

    val ttlAsInt: Int get() = ttl.epochSeconds.toInt()

    override fun signed(privateKey: PrivateKey): DhtValue =
        copy(signature = privateKey.sign(tlCodec().encodeToByteArray(this)))

    override fun verify(publicKey: PublicKey): Boolean =
        publicKey.verify(tlCodec().encodeToByteArray(copy(signature = ByteArray(0))), signature)

    override fun tlCodec(): TlCodec<DhtValue> = DhtValue

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DhtValue) return false
        if (key != other.key) return false
        if (!value.contentEquals(other.value)) return false
        if (ttl != other.ttl) return false
        if (!signature.contentEquals(other.signature)) return false
        return true
    }

    override fun hashCode(): Int {
        var result = key.hashCode()
        result = 31 * result + value.contentHashCode()
        result = 31 * result + ttlAsInt
        result = 31 * result + signature.contentHashCode()
        return result
    }

    public companion object : TlCodec<DhtValue> by DhtValueTlConstructor
}

private object DhtValueTlConstructor : TlConstructor<DhtValue>(
    schema = "dht.value key:dht.keyDescription value:bytes ttl:int signature:bytes = dht.Value"
) {
    override fun encode(output: TlWriter, value: DhtValue) {
        output.write(DhtKeyDescription, value.key)
        output.writeBytes(value.value)
        output.writeInt(value.ttlAsInt)
        output.writeBytes(value.signature)
    }

    override fun decode(input: TlReader): DhtValue {
        val key = input.read(DhtKeyDescription)
        val value = input.readBytes()
        val ttl = input.readInt()
        val signature = input.readBytes()
        return DhtValue(key, value, ttl, signature)
    }
}
