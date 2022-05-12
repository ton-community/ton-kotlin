package org.ton.api.adnl

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.crypto.Base64ByteArraySerializer
import org.ton.crypto.base64
import org.ton.tl.TlConstructor

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
        override fun encode(output: Output, message: AdnlProxyToSign) {
            output.writeIntLittleEndian(message.ip)
            output.writeIntLittleEndian(message.port)
            output.writeIntLittleEndian(message.date)
            output.writeBits256(message.signature)
        }

        override fun decode(input: Input): AdnlProxyToSign {
            val ip = input.readIntLittleEndian()
            val port = input.readIntLittleEndian()
            val date = input.readIntLittleEndian()
            val signature = input.readBits256()
            return AdnlProxyToSign(ip, port, date, signature)
        }
    }
}
