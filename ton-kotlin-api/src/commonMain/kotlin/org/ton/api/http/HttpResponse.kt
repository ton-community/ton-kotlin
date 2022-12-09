package org.ton.api.http

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.TlObject
import org.ton.tl.constructors.*

@SerialName("http.response")
@Serializable
data class HttpResponse(
    val http_version: String,
    val status_code: Int,
    val reason: String,
    val headers: List<HttpHeader>,
    val no_payload: Boolean
) : TlObject<HttpResponse> {
    override fun tlCodec(): TlCodec<HttpResponse> = Companion

    companion object : TlCodec<HttpResponse> by HttpResponseTlConstructor
}

private object HttpResponseTlConstructor : TlConstructor<HttpResponse>(
    schema = "http.response http_version:string status_code:int reason:string headers:(vector http.header) no_payload:Bool = http.Response"
) {
    override fun decode(input: Input): HttpResponse {
        val http_version = input.readStringTl()
        val status_code = input.readIntTl()
        val reason = input.readStringTl()
        val headers = input.readVectorTl(HttpHeader)
        val on_payload = input.readBoolTl()
        return HttpResponse(http_version, status_code, reason, headers, on_payload)
    }

    override fun encode(output: Output, value: HttpResponse) {
        output.writeStringTl(value.http_version)
        output.writeIntTl(value.status_code)
        output.writeStringTl(value.reason)
        output.writeVectorTl(value.headers, HttpHeader)
        output.writeBoolTl(value.no_payload)
    }
}
