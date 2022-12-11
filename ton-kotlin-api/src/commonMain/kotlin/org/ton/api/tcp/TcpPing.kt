package org.ton.api.tcp

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readLongTl
import org.ton.tl.constructors.writeLongTl

@Serializable
@SerialName("tcp.ping")
data class TcpPing(
    @SerialName("random_id")
    val randomId: Long
) {
    companion object : TlCodec<TcpPing> by TcpPingTlConstructor
}

private object TcpPingTlConstructor : TlConstructor<TcpPing>(
    schema = "tcp.ping random_id:long = tcp.Pong"
) {
    override fun decode(input: Input): TcpPing {
        val randomId = input.readLongTl()
        return TcpPing(randomId)
    }

    override fun encode(output: Output, value: TcpPing) {
        output.writeLongTl(value.randomId)
    }
}
