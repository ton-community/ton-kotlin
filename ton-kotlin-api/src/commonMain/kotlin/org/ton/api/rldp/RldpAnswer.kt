package org.ton.api.rldp

import io.ktor.util.*
import io.ktor.utils.io.core.*
import org.ton.bitstring.BitString
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.*

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

    override fun toString(): String =
        "RldpAnswer(query_id=$query_id, data=[(${data.size} bytes) ${data.encodeBase64()}])"

    companion object : TlConstructor<RldpAnswer>(
        schema = "rldp.answer query_id:int256 data:bytes = rldp.Message",
    ) {
        override fun encode(output: Output, value: RldpAnswer) {
            output.writeInt256Tl(value.query_id)
            output.writeBytesTl(value.data)
        }

        override fun decode(input: Input): RldpAnswer {
            val query_id = input.readInt256Tl()
            val data = input.readBytesTl()
            return RldpAnswer(BitString(query_id), data)
        }
    }
}