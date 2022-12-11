package org.ton.api.tcp

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readLongTl
import org.ton.tl.constructors.writeLongTl

@Serializable
@SerialName("tcp.pong")
data class TcpPong(
    @SerialName("random_id")
    val randomId: Long
) {
    companion object : TlCodec<TcpPong> by TcpPongTlConstructor
}

private object TcpPongTlConstructor : TlConstructor<TcpPong>(
    schema = "tcp.pong random_id:long = tcp.Pong"
) {
    override fun decode(input: Input): TcpPong {
        val randomId = input.readLongTl()
        return TcpPong(randomId)
    }

    override fun encode(output: Output, value: TcpPong) {
        output.writeLongTl(value.randomId)
    }
}
