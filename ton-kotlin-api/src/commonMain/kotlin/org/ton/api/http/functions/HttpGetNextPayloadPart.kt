package org.ton.api.http.functions

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.http.HttpPayloadPart
import org.ton.tl.*
import org.ton.tl.constructors.*

@SerialName("http.getNextPayloadPart")
@Serializable
public data class HttpGetNextPayloadPart(
    val id: Bits256,
    val seqno: Int,
    @SerialName("max_chunk_size")
    val maxChunkSize: Int
) : TLFunction<HttpGetNextPayloadPart, HttpPayloadPart> {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is HttpGetNextPayloadPart) return false

        if (id != other.id) return false
        if (seqno != other.seqno) return false
        if (maxChunkSize != other.maxChunkSize) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + seqno
        result = 31 * result + maxChunkSize
        return result
    }

    override fun tlCodec(): TlCodec<HttpGetNextPayloadPart> = Companion
    override fun resultTlCodec(): TlCodec<HttpPayloadPart> = HttpPayloadPart

    public companion object : TlCodec<HttpGetNextPayloadPart> by HttpGetNextPayloadPartTlConstructor
}

private object HttpGetNextPayloadPartTlConstructor : TlConstructor<HttpGetNextPayloadPart>(
    schema = "http.getNextPayloadPart id:int256 seqno:int max_chunk_size:int = http.PayloadPart"
) {
    override fun decode(input: TlReader): HttpGetNextPayloadPart {
        val id = input.readBits256()
        val seqno = input.readInt()
        val max_chunk_size = input.readInt()
        return HttpGetNextPayloadPart(id, seqno, max_chunk_size)
    }

    override fun encode(output: TlWriter, value: HttpGetNextPayloadPart) {
        output.writeBits256(value.id)
        output.writeInt(value.seqno)
        output.writeInt(value.maxChunkSize)
    }
}
