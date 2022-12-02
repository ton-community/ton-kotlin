package org.ton.api.adnl

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.crypto.base64.Base64ByteArraySerializer
import org.ton.crypto.base64.base64
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readInt256Tl
import org.ton.tl.constructors.readIntTl
import org.ton.tl.constructors.writeInt256Tl
import org.ton.tl.constructors.writeIntTl

@Serializable
data class AdnlProxyToSign(
    val ip: Int,
    val port: Int,
    val date: Int,
    @Serializable(Base64ByteArraySerializer::class)
    val signature: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AdnlProxyToSign

        if (ip != other.ip) return false
        if (port != other.port) return false
        if (date != other.date) return false
        if (!signature.contentEquals(other.signature)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = ip
        result = 31 * result + port
        result = 31 * result + date
        result = 31 * result + signature.contentHashCode()
        return result
    }

    override fun toString(): String = buildString {
        append("AdnlProxyToSign(ip=")
        append(ip)
        append(", port=")
        append(port)
        append(", date=")
        append(date)
        append(", signature=")
        append(base64(signature))
        append(")")
    }

    companion object : TlConstructor<AdnlProxyToSign>(
        type = AdnlProxyToSign::class,
        schema = "adnl.proxyToFast ip:int port:int date:int signature:int256 = adnl.ProxyToSign"
    ) {
        override fun encode(output: Output, value: AdnlProxyToSign) {
            output.writeIntTl(value.ip)
            output.writeIntTl(value.port)
            output.writeIntTl(value.date)
            output.writeInt256Tl(value.signature)
        }

        override fun decode(input: Input): AdnlProxyToSign {
            val ip = input.readIntTl()
            val port = input.readIntTl()
            val date = input.readIntTl()
            val signature = input.readInt256Tl()
            return AdnlProxyToSign(ip, port, date, signature)
        }
    }
}
