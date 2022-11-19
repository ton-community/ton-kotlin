package org.ton.api.http

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readStringTl
import org.ton.tl.constructors.writeStringTl

@SerialName("http.header")
@Serializable
data class HttpHeader(
    val name: String,
    val value: String
) {
    companion object : TlCodec<HttpHeader> by HttpHeaderTlConstructor
}

operator fun Iterable<HttpHeader>.get(name: String) = firstOrNull { it.name == name }?.value
fun Iterable<HttpHeader>.getAll(name: String) = asSequence().filter { it.name == name }.map { it.value }

private object HttpHeaderTlConstructor : TlConstructor<HttpHeader>(
    type = HttpHeader::class,
    schema = "http.header name:string value:string = http.Header"
) {
    override fun decode(input: Input): HttpHeader {
        val name = input.readStringTl()
        val value = input.readStringTl()
        return HttpHeader(name, value)
    }

    override fun encode(output: Output, value: HttpHeader) {
        output.writeStringTl(value.name)
        output.writeStringTl(value.value)
    }
}
