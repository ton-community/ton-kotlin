package org.ton.api.rldp

import io.ktor.utils.io.core.*
import org.ton.bitstring.BitString
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.BytesTlConstructor
import org.ton.tl.constructors.Int256TlConstructor
import org.ton.tl.constructors.writeBytesTl
import org.ton.tl.constructors.writeInt256Tl

data class RldpAnswer(
    val query_id: BitString,
    override val data: ByteArray
) : RldpMessage {
    override val id: BitString
        get() = query_id

    override fun tlCodec(): TlCodec<RldpAnswer> = Companion

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RldpAnswer) return false

        if (query_id != other.query_id) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = query_id.hashCode()
        result = 31 * result + data.contentHashCode()
        return result
    }

    companion object : TlConstructor<RldpAnswer>(
        type = RldpAnswer::class,
        schema = "rldp.answer query_id:int256 data:bytes = rldp.Message",
        fields = listOf(Int256TlConstructor, BytesTlConstructor)
    ) {
        override fun encode(output: Output, value: RldpAnswer) {
            output.writeInt256Tl(value.query_id)
            output.writeBytesTl(value.data)
        }

        override fun decode(values: Iterator<*>): RldpAnswer {
            val query_id = values.next() as ByteArray
            val data = values.next() as ByteArray
            return RldpAnswer(BitString(query_id), data)
        }
    }
}
