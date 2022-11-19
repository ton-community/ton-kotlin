package org.ton.proxy

import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.cio.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.ton.api.adnl.AdnlIdShort
import org.ton.api.http.HttpHeader
import org.ton.api.http.HttpResponse
import org.ton.api.http.functions.HttpGetNextPayloadPart
import org.ton.api.http.functions.HttpRequest
import org.ton.api.liteclient.config.LiteClientConfigGlobal
import org.ton.bitstring.BitString
import org.ton.block.DnsAdnlAddress
import org.ton.lite.client.LiteClient
import org.ton.proxy.adnl.engine.CIOAdnlNetworkEngine
import org.ton.proxy.dht.Dht
import org.ton.proxy.dns.DnsCategory
import org.ton.proxy.dns.DnsResolver
import org.ton.proxy.rldp.Rldp
import java.util.*
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

class ProxyClient(configGlobal: LiteClientConfigGlobal) {
    val dht = Dht.lite(configGlobal.dht.static_nodes)
    val liteClient = LiteClient(configGlobal)
    val dnsResolver = DnsResolver(liteClient)
    val rldp = Rldp(CIOAdnlNetworkEngine(), dht)

    val app = embeddedServer(CIO, port = 8080, module = {
        proxyModule()
    })

    fun Application.proxyModule() {
        intercept(ApplicationCallPipeline.Call) {
            try {
                val request = call.request
                val path = request.path()

                val domain = if (path.startsWith(HTTP_PROTOCOL)) {
                    path.substring(HTTP_PROTOCOL.length).substringBefore('/')
                } else {
                    path.substring(HTTPS_PROTOCOL.length).substringBefore('/')
                }
                if (domain.endsWith(".adnl") || domain.endsWith(".ton")) {
                    val dnsRecord = dnsResolver.resolve(domain, DnsCategory.SITE) as? DnsAdnlAddress
                    if (dnsRecord == null) {
                        call.respond(HttpStatusCode.NotFound, "Can't resolve domain: $domain")
                        return@intercept
                    }
                    val address = AdnlIdShort(dnsRecord.adnl_addr)
                    val adnlAddressList = dht.resolve(address)
                    if (adnlAddressList == null) {
                        call.respond(HttpStatusCode.NotFound, "Can't resolve ADNL address: $address")
                        return@intercept
                    }
                    val rldpHttpId = BitString(Random.nextBytes(32))
                    val rldpHttpRequest = HttpRequest(
                        id = rldpHttpId,
                        method = call.request.httpMethod.value,
                        url = call.request.uri,
                        http_version = "HTTP/1.1",
                        headers = call.request.headers.entries()
                            .filter { it.key == "Host" }
                            .map { (key, values) ->
                                values.map { value ->
                                    HttpHeader(key, value)
                                }
                            }.flatten()
                    )
                    println("Sending RLDP query: ${Json.encodeToString(rldpHttpRequest)}")
                    val rldpHttpResponse = rldp.query(address, rldpHttpRequest, 120.seconds)
                    println("Received RLDP response: ${Json.encodeToString(rldpHttpResponse)}")
                    rldpHttpResponse.headers.forEach { header ->
                        call.response.headers.append(header.name, header.value)
                    }
                    val statusCode = HttpStatusCode.fromValue(rldpHttpResponse.status_code)
                    call.response.status(statusCode)
                    if (rldpHttpResponse.no_payload) {
                        call.respondNullable(NullBody)
                    } else {
                        call.respondRldpPayload(address, rldpHttpRequest.id)
                    }
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Only .adnl and .ton domains are supported")
                }
            } catch (t: Throwable) {
                t.printStackTrace()
                throw t
            }
        }
    }

    fun payloadFlow(address: AdnlIdShort, id: BitString) = flow {
        var isLast = false
        var seqno = 0
        while (!isLast) {
            val rldpHttpPayloadPart = rldp.query(
                destination = address,
                query = HttpGetNextPayloadPart(
                    id = id,
                    seqno = seqno++,
                    max_chunk_size = PAYLOAD_SIZE
                ),
                timeout = 120.seconds,
                maxAnswerSize = MAX_RLDP_ANSWER_SIZE
            )
            println("[$id] seqno:$seqno - $rldpHttpPayloadPart")
            emit(rldpHttpPayloadPart)
            isLast = rldpHttpPayloadPart.last
        }
    }

    suspend fun getRequest(
        address: AdnlIdShort,
        host: String,
        url: String,
        headers: List<HttpHeader> = emptyList(),
        id: BitString = BitString(ByteArray(32))
    ): HttpResponse {
        val request = HttpRequest(
            id = id,
            method = "GET",
            url = url,
            http_version = "HTTP/1.1",
            headers = (listOf(
                HttpHeader("Host", host),
            ) + headers).distinct()
        )
        return rldp.query(address, request, 120.seconds)
    }

    suspend fun ApplicationCall.respondRldpPayload(
        address: AdnlIdShort,
        id: BitString,
        payloadPartSize: Int = -1,
    ) = coroutineScope {

        val acceptRanges = response.headers[HttpHeaders.AcceptRanges] == "bytes"
        val contentLength = response.headers[HttpHeaders.ContentLength]?.toLong()
        val contentType = response.headers[HttpHeaders.ContentType]?.let { ContentType.parse(it) }
        if (payloadPartSize > 0 && acceptRanges && contentLength != null && contentLength > payloadPartSize) {
//            println("start downloading with offsets: $contentLength - ${request.url}")
//            val bytes = ByteArray(contentLength.toInt())
//            val payloadParts = (contentLength / payloadPartSize).toInt() + (contentLength % payloadPartSize).toInt()
//            val offsetRequests = List(payloadParts) { offset ->
//                launch {
//                    val newHeaders = request.headers.filter {
//                        it.name != "Range"
//                    } + HttpHeader("Range", "bytes=${offset * payloadPartSize}-${(offset + 1) * payloadPartSize - 1}")
//                    val offsetRequest = request.copy(
//                        id = BitString(Random.nextBytes(32)),
//                        headers = newHeaders
//                    )
//                    rldp.query(address, offsetRequest, 120.seconds)
//                    payloadFlow(address, offsetRequest.id).buffer().toList().forEach {
//                        it.data.copyInto(bytes, offset * payloadPartSize)
//                    }
//                }
//            }
//            offsetRequests.joinAll()
//            bytes
        } else {
            println("start downloading without offsets - ${request.uri} [$payloadPartSize]")
            val httpPayloadPartFlow = payloadFlow(address, id).buffer()
            respondBytesWriter(contentType, null, contentLength) {
                httpPayloadPartFlow.collect { payloadPart ->
                    writeFully(payloadPart.data)
                }
            }
        }
    }

    fun start(wait: Boolean = true) {
        app.start(wait)
    }

    companion object {
        @Suppress("HttpUrlsUsage")
        private const val HTTP_PROTOCOL = "http://"
        private const val HTTPS_PROTOCOL = "https://"
        private const val PAYLOAD_SIZE = 1 shl 20
        private const val MAX_RLDP_ANSWER_SIZE = PAYLOAD_SIZE * 2L + 1024L
    }
}
