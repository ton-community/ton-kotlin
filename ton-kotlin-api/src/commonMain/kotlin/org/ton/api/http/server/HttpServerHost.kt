package org.ton.api.http.server

import io.ktor.utils.io.core.*
import org.ton.api.adnl.AdnlIdShort
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.*
import org.ton.tl.readTl
import org.ton.tl.writeTl

data class HttpServerHost(
    val domains: List<String>,
    val ip: Int,
    val port: Int,
    val adnl_id: AdnlIdShort
) {
    companion object : TlCodec<HttpServerHost> by HttpServerHostTlConstructor
}

private object HttpServerHostTlConstructor : TlConstructor<HttpServerHost>(
    schema = "http.server.host domains:(vector string) ip:int32 port:int32 adnl_id:adnl.id.short = http.server.Host"
) {
    override fun decode(input: Input): HttpServerHost {
        val domains = input.readVectorTl(StringTlConstructor)
        val ip = input.readIntTl()
        val port = input.readIntTl()
        val adnl_id = input.readTl(AdnlIdShort)
        return HttpServerHost(domains, ip, port, adnl_id)
    }

    override fun encode(output: Output, value: HttpServerHost) {
        output.writeVectorTl(value.domains, StringTlConstructor)
        output.writeIntTl(value.ip)
        output.writeIntTl(value.port)
        output.writeTl(AdnlIdShort, value.adnl_id)
    }
}
