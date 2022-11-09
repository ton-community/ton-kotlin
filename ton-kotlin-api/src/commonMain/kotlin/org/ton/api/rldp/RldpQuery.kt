@file:Suppress("LocalVariableName")

package org.ton.api.rldp

import io.ktor.utils.io.core.*
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.*

@Serializable
@SerialName("rldp.query")
data class RldpQuery(
    val query_id: BitString,
    val max_answer_size: Long,
    val timeout: Int,
    override val data: ByteArray
) : RldpMessage {
    constructor(
        query_id: BitString,
        max_answer_size: Long,
        timeout: Instant,
        data: ByteArray
    ) : this(query_id, max_answer_size, timeout.epochSeconds.toInt(), data)

    override val id: BitString
        get() = query_id

    override fun tlCodec(): TlCodec<RldpQuery> = Companion

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RldpQuery) return false

        if (query_id != other.query_id) return false
        if (max_answer_size != other.max_answer_size) return false
        if (timeout != other.timeout) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = query_id.hashCode()
        result = 31 * result + max_answer_size.hashCode()
        result = 31 * result + timeout
        result = 31 * result + data.contentHashCode()
        return result
    }

    companion object : TlConstructor<RldpQuery>(
        type = RldpQuery::class,
        schema = "rldp.query query_id:int256 max_answer_size:long timeout:int data:bytes = rldp.Message",
        fields = listOf(Int256TlConstructor, LongTlConstructor, IntTlConstructor, BytesTlConstructor)
    ) {
        override fun encode(output: Output, value: RldpQuery) {
            output.writeInt256Tl(value.query_id)
            output.writeLongTl(value.max_answer_size)
            output.writeIntTl(value.timeout)
            output.writeBytesTl(value.data)
        }

        override fun decode(values: Iterator<*>): RldpQuery {
            val query_id = values.next() as ByteArray
            val max_answer_size = values.next() as Long
            val timeout = values.next() as Int
            val data = values.next() as ByteArray
            return RldpQuery(BitString(query_id), max_answer_size, timeout, data)
        }
    }
}
