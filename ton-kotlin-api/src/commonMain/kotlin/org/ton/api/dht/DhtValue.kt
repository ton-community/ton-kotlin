package org.ton.api.dht

import kotlinx.serialization.Serializable
import org.ton.api.SignedTlObject
import org.ton.api.pk.PrivateKey
import org.ton.api.pub.PublicKey
import org.ton.tl.*

@Serializable
public data class DhtValue(
    val key: DhtKeyDescription,
    val value: ByteArray,
    val ttl: Int,
    override val signature: ByteArray = ByteArray(0)
) : SignedTlObject<DhtValue> {

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
        result = 31 * result + ttl
        result = 31 * result + signature.contentHashCode()
        return result
    }

    public companion object : TlCodec<DhtValue> by DhtValueTlConstructor
}

private object DhtValueTlConstructor : TlConstructor<DhtValue>(
    schema = "dht.value key:dht.keyDescription value:bytes ttl:int signature:bytes = dht.Value"
) {
    override fun encode(writer: TlWriter, value: DhtValue) {
        writer.write(DhtKeyDescription, value.key)
        writer.writeBytes(value.value)
        writer.writeInt(value.ttl)
        writer.writeBytes(value.signature)
    }

    override fun decode(reader: TlReader): DhtValue {
        val key = reader.read(DhtKeyDescription)
        val value = reader.readBytes()
        val ttl = reader.readInt()
        val signature = reader.readBytes()
        return DhtValue(key, value, ttl, signature)
    }
}
