package org.ton.api.http.server

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.adnl.AdnlIdShort
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readStringTl
import org.ton.tl.constructors.writeStringTl
import org.ton.tl.readTl
import org.ton.tl.writeTl

@SerialName("http.server.dnsEntry")
@Serializable
data class HttpServerDnsEntry(
    val domain: String,
    val addr: AdnlIdShort
) {
    companion object : TlCodec<HttpServerDnsEntry> by HttpServerDnsEntryTlConstructor
}

private object HttpServerDnsEntryTlConstructor : TlConstructor<HttpServerDnsEntry>(
    schema = "http.server.dnsEntry domain:string addr:adnl.id_short = http.server.DnsEntry"
) {
    override fun decode(input: Input): HttpServerDnsEntry {
        val domain = input.readStringTl()
        val addr = input.readTl(AdnlIdShort)
        return HttpServerDnsEntry(domain, addr)
    }

    override fun encode(output: Output, value: HttpServerDnsEntry) {
        output.writeStringTl(value.domain)
        output.writeTl(AdnlIdShort, value.addr)
    }
}
