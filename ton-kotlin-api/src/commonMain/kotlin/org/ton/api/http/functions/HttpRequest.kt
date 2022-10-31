package org.ton.api.http.functions

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.http.HttpHeader
import org.ton.api.http.HttpResponse
import org.ton.crypto.HexByteArraySerializer
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.*

fun interface HttpRequestFunction {
    suspend fun httpRequest(request: HttpRequest): HttpResponse
}

@SerialName("http.request")
@Serializable
data class HttpRequest(
    @Serializable(HexByteArraySerializer::class)
    val id: ByteArray,
    val method: String,
    val url: String,
    val http_version: String,
    val headers: List<HttpHeader>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is HttpRequest) return false

        if (!id.contentEquals(other.id)) return false
        if (method != other.method) return false
        if (url != other.url) return false
        if (http_version != other.http_version) return false
        if (headers != other.headers) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.contentHashCode()
        result = 31 * result + method.hashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + http_version.hashCode()
        result = 31 * result + headers.hashCode()
        return result
    }
}

private object HttpRequestTlConstructor : TlConstructor<HttpRequest>(
    type = HttpRequest::class,
    schema = "http.request id:int256 method:string url:string http_version:string headers:(vector http.Header) = http.Response"
) {
    override fun decode(input: Input): HttpRequest {
        val id = input.readBytesTl()
        val method = input.readStringTl()
        val url = input.readStringTl()
        val http_version = input.readStringTl()
        val headers = input.readVectorTl(HttpHeader)
        return HttpRequest(id, method, url, http_version, headers)
    }

    override fun encode(output: Output, value: HttpRequest) {
        output.writeBytesTl(value.id)
        output.writeStringTl(value.method)
        output.writeStringTl(value.url)
        output.writeStringTl(value.http_version)
        output.writeVectorTl(value.headers, HttpHeader)
    }
}
