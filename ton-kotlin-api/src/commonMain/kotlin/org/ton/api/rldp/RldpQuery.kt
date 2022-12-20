@file:Suppress("LocalVariableName")

package org.ton.api.rldp

import io.ktor.utils.io.core.*
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.bitstring.toBitString
import org.ton.tl.*
import org.ton.tl.constructors.*

@Serializable
@SerialName("rldp.query")
public data class RldpQuery(
    val query_id: Bits256,
    val max_answer_size: Long,
    val timeout: Int,
    override val data: ByteArray
) : RldpMessage {
    public constructor(
        query_id: ByteArray,
        max_answer_size: Long,
        timeout: Instant,
        data: ByteArray
    ) : this(Bits256(query_id), max_answer_size, timeout.epochSeconds.toInt(), data)

    override val id: Bits256
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

    public companion object : TlConstructor<RldpQuery>(
        schema = "rldp.query query_id:int256 max_answer_size:long timeout:int data:bytes = rldp.Message",
    ) {
        override fun encode(writer: TlWriter, value: RldpQuery) {
            writer.writeBits256(value.query_id)
            writer.writeLong(value.max_answer_size)
            writer.writeInt(value.timeout)
            writer.writeBytes(value.data)
        }

        override fun decode(reader: TlReader): RldpQuery {
            val query_id = reader.readBits256()
            val max_answer_size = reader.readLong()
            val timeout = reader.readInt()
            val data = reader.readBytes()
            return RldpQuery(query_id, max_answer_size, timeout, data)
        }
    }
}
