package org.ton.api.rldp

import io.ktor.utils.io.core.*
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.Bits256
import org.ton.tl.*
import org.ton.tl.constructors.*

@Serializable
@SerialName("rldp.query")
public data class RldpQuery(
    @SerialName("query_id")
    val queryId: Bits256,
    @SerialName("max_answer_size")
    val maxAnswerSize: Long,
    val timeout: Int,
    override val data: ByteArray
) : RldpMessage {
    public constructor(
        queryId: ByteArray,
        maxAnswerSize: Long,
        timeout: Instant,
        data: ByteArray
    ) : this(Bits256(queryId), maxAnswerSize, timeout.epochSeconds.toInt(), data)

    override val id: Bits256
        get() = queryId

    override fun tlCodec(): TlCodec<RldpQuery> = Companion

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RldpQuery) return false

        if (queryId != other.queryId) return false
        if (maxAnswerSize != other.maxAnswerSize) return false
        if (timeout != other.timeout) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = queryId.hashCode()
        result = 31 * result + maxAnswerSize.hashCode()
        result = 31 * result + timeout
        result = 31 * result + data.contentHashCode()
        return result
    }

    public companion object : TlConstructor<RldpQuery>(
        schema = "rldp.query query_id:int256 max_answer_size:long timeout:int data:bytes = rldp.Message",
    ) {
        override fun encode(writer: TlWriter, value: RldpQuery) {
            writer.writeBits256(value.queryId)
            writer.writeLong(value.maxAnswerSize)
            writer.writeInt(value.timeout)
            writer.writeBytes(value.data)
        }

        override fun decode(reader: TlReader): RldpQuery {
            val queryId = reader.readBits256()
            val maxAnswerSize = reader.readLong()
            val timeout = reader.readInt()
            val data = reader.readBytes()
            return RldpQuery(queryId, maxAnswerSize, timeout, data)
        }
    }
}
