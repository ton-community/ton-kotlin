package org.ton.api.http.functions

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.http.HttpPayloadPart
import org.ton.crypto.HexByteArraySerializer
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readBytesTl
import org.ton.tl.constructors.readIntTl
import org.ton.tl.constructors.writeBytesTl
import org.ton.tl.constructors.writeIntTl

fun interface HttpGetNextPayloadPartFunction {
    suspend fun getNextPayloadPart(query: HttpGetNextPayloadPart): HttpPayloadPart
}

@SerialName("http.getNextPayloadPart")
@Serializable
data class HttpGetNextPayloadPart(
    @Serializable(HexByteArraySerializer::class)
    val id: ByteArray,
    val seqno: Int,
    val max_chunk_size: Int
) {
    companion object : TlCodec<HttpGetNextPayloadPart> by HttpGetNextPayloadPartTlConstructor

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is HttpGetNextPayloadPart) return false

        if (!id.contentEquals(other.id)) return false
        if (seqno != other.seqno) return false
        if (max_chunk_size != other.max_chunk_size) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.contentHashCode()
        result = 31 * result + seqno
        result = 31 * result + max_chunk_size
        return result
    }
}

private object HttpGetNextPayloadPartTlConstructor : TlConstructor<HttpGetNextPayloadPart>(
    type = HttpGetNextPayloadPart::class,
    schema = "http.get_next_payload_part id:int256 seqno:int max_chunk_size:int = http.PayloadPart"
) {
    override fun decode(input: Input): HttpGetNextPayloadPart {
        val id = input.readBytesTl()
        val seqno = input.readIntTl()
        val max_chunk_size = input.readIntTl()
        return HttpGetNextPayloadPart(id, seqno, max_chunk_size)
    }

    override fun encode(output: Output, value: HttpGetNextPayloadPart) {
        output.writeBytesTl(value.id)
        output.writeIntTl(value.seqno)
        output.writeIntTl(value.max_chunk_size)
    }
}
