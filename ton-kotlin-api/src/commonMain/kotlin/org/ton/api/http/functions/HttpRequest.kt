package org.ton.api.http.functions

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.http.HttpHeader
import org.ton.api.http.HttpResponse
import org.ton.bitstring.BitString
import org.ton.tl.TLFunction
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.*

@SerialName("http.request")
@Serializable
data class HttpRequest(
    val id: BitString,
    val method: String,
    val url: String,
    val http_version: String,
    val headers: List<HttpHeader>
) : TLFunction<HttpRequest, HttpResponse> {
    override fun tlCodec(): TlCodec<HttpRequest> = Companion

    override fun resultTlCodec(): TlCodec<HttpResponse> = HttpResponse

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is HttpRequest) return false

        if (id != other.id) return false
        if (method != other.method) return false
        if (url != other.url) return false
        if (http_version != other.http_version) return false
        if (headers != other.headers) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + method.hashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + http_version.hashCode()
        result = 31 * result + headers.hashCode()
        return result
    }

    companion object : TlConstructor<HttpRequest>(
        type = HttpRequest::class,
        schema = "http.request id:int256 method:string url:string http_version:string headers:(vector http.header) = http.Response"
    ) {
        override fun decode(input: Input): HttpRequest {
            val id = input.readInt256Tl()
            val method = input.readStringTl()
            val url = input.readStringTl()
            val http_version = input.readStringTl()
            val headers = input.readVectorTl(HttpHeader)
            return HttpRequest(BitString(id), method, url, http_version, headers)
        }

        override fun encode(output: Output, value: HttpRequest) {
            output.writeInt256Tl(value.id)
            output.writeStringTl(value.method)
            output.writeStringTl(value.url)
            output.writeStringTl(value.http_version)
            output.writeVectorTl(value.headers, HttpHeader)
        }
    }
}
