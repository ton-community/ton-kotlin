package org.ton.api.http.functions

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.http.HttpPayloadPart
import org.ton.bitstring.BitString
import org.ton.tl.TLFunction
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.*

@SerialName("http.getNextPayloadPart")
@Serializable
data class HttpGetNextPayloadPart(
    val id: BitString,
    val seqno: Int,
    val max_chunk_size: Int
) : TLFunction<HttpGetNextPayloadPart, HttpPayloadPart> {
    init {
        require(id.size == 256) { "id must be 256 bits long" }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is HttpGetNextPayloadPart) return false

        if (id != other.id) return false
        if (seqno != other.seqno) return false
        if (max_chunk_size != other.max_chunk_size) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + seqno
        result = 31 * result + max_chunk_size
        return result
    }

    override fun tlCodec(): TlCodec<HttpGetNextPayloadPart> = Companion
    override fun resultTlCodec(): TlCodec<HttpPayloadPart> = HttpPayloadPart

    companion object : TlCodec<HttpGetNextPayloadPart> by HttpGetNextPayloadPartTlConstructor
}

private object HttpGetNextPayloadPartTlConstructor : TlConstructor<HttpGetNextPayloadPart>(
    schema = "http.getNextPayloadPart id:int256 seqno:int max_chunk_size:int = http.PayloadPart"
) {
    override fun decode(input: Input): HttpGetNextPayloadPart {
        val id = input.readInt256Tl()
        val seqno = input.readIntTl()
        val max_chunk_size = input.readIntTl()
        return HttpGetNextPayloadPart(BitString(id), seqno, max_chunk_size)
    }

    override fun encode(output: Output, value: HttpGetNextPayloadPart) {
        output.writeInt256Tl(value.id)
        output.writeIntTl(value.seqno)
        output.writeIntTl(value.max_chunk_size)
    }
}
