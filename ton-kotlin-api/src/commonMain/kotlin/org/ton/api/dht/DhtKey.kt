package org.ton.api.dht

import io.ktor.util.*
import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.api.adnl.AdnlIdShort
import org.ton.crypto.Base64ByteArraySerializer
import org.ton.crypto.base64
import org.ton.crypto.encodeHex
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.TlObject
import org.ton.tl.constructors.*

@Serializable
data class DhtKey(
    @Serializable(Base64ByteArraySerializer::class)
    val id: ByteArray,
    val name: String,
    val idx: Int = 0
) : TlObject<DhtKey> {
    constructor(id: AdnlIdShort, name: String, idx: Int = 0) : this(id.id, name, idx)

    override fun tlCodec(): TlCodec<DhtKey> = DhtKey

    fun key(): ByteArray = hash(this)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DhtKey

        if (!id.contentEquals(other.id)) return false
        if (name != other.name) return false
        if (idx != other.idx) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.contentHashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + idx
        return result
    }

    override fun toString(): String = buildString {
        append("DhtKey(id=")
        append(base64(id))
        append(", name='")
        append(name)
        append("', idx=")
        append(idx)
        append(")")
    }

    companion object : TlConstructor<DhtKey>(
        type = DhtKey::class,
        schema = "dht.key id:int256 name:bytes idx:int = dht.Key"
    ) {
        @JvmStatic
        fun address(adnlIdShort: AdnlIdShort) = DhtKey(adnlIdShort, "address")

        @JvmStatic
        fun nodes(adnlIdShort: AdnlIdShort) = DhtKey(adnlIdShort, "nodes")

        override fun encode(output: Output, value: DhtKey) {
            output.writeInt256Tl(value.id)
            output.writeBytesTl(value.name.encodeToByteArray())
            output.writeIntTl(value.idx)
        }

        override fun decode(input: Input): DhtKey {
            val id = input.readInt256Tl()
            println(id.encodeBase64())
            println(id.encodeHex())
            val name = input.readBytesTl().decodeToString()
            val idx = input.readIntTl()
            return DhtKey(id, name, idx)
        }
    }
}
