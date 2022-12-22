package org.ton.api.http

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.tl.*
import org.ton.tl.constructors.*

@SerialName("http.response")
@Serializable
public data class HttpResponse(
    @SerialName("http_version")
    val httpVersion: String,
    @SerialName("status_code")
    val statusCode: Int,
    val reason: String,
    val headers: Collection<HttpHeader>,
    @SerialName("no_payload")
    val noPayload: Boolean
) : TlObject<HttpResponse> {
    override fun tlCodec(): TlCodec<HttpResponse> = Companion

    public companion object : TlCodec<HttpResponse> by HttpResponseTlConstructor
}

private object HttpResponseTlConstructor : TlConstructor<HttpResponse>(
    schema = "http.response http_version:string status_code:int reason:string headers:(vector http.header) no_payload:Bool = http.Response"
) {
    override fun decode(input: TlReader): HttpResponse {
        val http_version = input.readString()
        val status_code = input.readInt()
        val reason = input.readString()
        val headers = input.readCollection {
            read(HttpHeader)
        }
        val on_payload = input.readBoolean()
        return HttpResponse(http_version, status_code, reason, headers, on_payload)
    }

    override fun encode(output: TlWriter, value: HttpResponse) {
        output.writeString(value.httpVersion)
        output.writeInt(value.statusCode)
        output.writeString(value.reason)
        output.writeCollection(value.headers) {
            write(HttpHeader, it)
        }
        output.writeBoolean(value.noPayload)
    }
}
