package org.ton.api.dht

import kotlinx.serialization.Serializable
import org.ton.api.SignedTlObject
import org.ton.api.pk.PrivateKey
import org.ton.api.pub.PublicKey
import org.ton.tl.*
import kotlin.jvm.JvmName

@Serializable
public data class DhtValue(
    @get:JvmName("key")
    val key: DhtKeyDescription,
    val value: ByteString,
    val ttl: Int,
    override val signature: ByteString = ByteString.of()
) : SignedTlObject<DhtValue> {

    override fun signed(privateKey: PrivateKey): DhtValue =
        copy(signature = privateKey.sign(tlCodec().encodeToByteArray(this)).asByteString())

    override fun verify(publicKey: PublicKey): Boolean =
        publicKey.verify(tlCodec().encodeToByteArray(copy(signature = ByteArray(0).asByteString())), signature.toByteArray())

    override fun tlCodec(): TlCodec<DhtValue> = DhtValue

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
        val value = reader.readByteString()
        val ttl = reader.readInt()
        val signature = reader.readByteString()
        return DhtValue(key, value, ttl, signature)
    }
}
