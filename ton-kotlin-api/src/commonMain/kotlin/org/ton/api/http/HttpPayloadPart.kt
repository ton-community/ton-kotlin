package org.ton.api.http

import io.ktor.util.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.tl.*

@SerialName("http.payloadPart")
@Serializable
public data class HttpPayloadPart(
    val data: ByteArray,
    val trailer: Collection<HttpHeader>,
    val last: Boolean
) : TlObject<HttpPayloadPart> {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is HttpPayloadPart) return false

        if (!data.contentEquals(other.data)) return false
        if (trailer != other.trailer) return false
        if (last != other.last) return false

        return true
    }

    override fun hashCode(): Int {
        var result = data.contentHashCode()
        result = 31 * result + trailer.hashCode()
        result = 31 * result + last.hashCode()
        return result
    }

    override fun tlCodec(): TlCodec<HttpPayloadPart> = Companion

    override fun toString(): String =
        "HttpPayloadPart(trailer=$trailer, last=$last, data=(${data.size} bytes) ${data.encodeBase64()})"

    public companion object : TlCodec<HttpPayloadPart> by HttpPayloadPartTlConstructor
}

private object HttpPayloadPartTlConstructor : TlConstructor<HttpPayloadPart>(
    schema = "http.payloadPart data:bytes trailer:(vector http.header) last:Bool = http.PayloadPart"
) {
    override fun decode(reader: TlReader): HttpPayloadPart {
        val data = reader.readBytes()
        val trailer = reader.readVector {
            read(HttpHeader)
        }
        val last = reader.readBoolean()
        return HttpPayloadPart(data, trailer, last)
    }

    override fun encode(writer: TlWriter, value: HttpPayloadPart) {
        writer.writeBytes(value.data)
        writer.writeVector(value.trailer) {
            write(HttpHeader, it)
        }
        writer.writeBoolean(value.last)
    }
}
