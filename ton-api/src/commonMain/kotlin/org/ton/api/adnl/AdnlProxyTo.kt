package org.ton.api.adnl

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.crypto.Base64ByteArraySerializer
import org.ton.crypto.base64
import org.ton.tl.TlConstructor

@Serializable
data class AdnlProxyTo(
        val ip: Int,
        val port: Int,
        val date: Int,
        @SerialName("date_hash")
        @Serializable(Base64ByteArraySerializer::class)
        val dateHash: ByteArray,
        @SerialName("shared_secret")
        @Serializable(Base64ByteArraySerializer::class)
        val sharedSecret: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AdnlProxyTo

        if (ip != other.ip) return false
        if (port != other.port) return false
        if (date != other.date) return false
        if (!dateHash.contentEquals(other.dateHash)) return false
        if (!sharedSecret.contentEquals(other.sharedSecret)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = ip
        result = 31 * result + port
        result = 31 * result + date
        result = 31 * result + dateHash.contentHashCode()
        result = 31 * result + sharedSecret.contentHashCode()
        return result
    }

    override fun toString(): String = buildString {
        append("AdnlProxyTo(ip=")
        append(ip)
        append(", port=")
        append(port)
        append(", date=")
        append(date)
        append(", dateHash=")
        append(base64(dateHash))
        append(", sharedSecret=")
        append(base64(sharedSecret))
        append(")")
    }

    companion object : TlConstructor<AdnlProxyTo>(
            type = AdnlProxyTo::class,
            schema = "adnl.proxyToFastHash ip:int port:int date:int data_hash:int256 shared_secret:int256 = adnl.ProxyTo"
    ) {
        override fun encode(output: Output, message: AdnlProxyTo) {
            output.writeIntLittleEndian(message.ip)
            output.writeIntLittleEndian(message.port)
            output.writeIntLittleEndian(message.date)
            output.writeBits256(message.dateHash)
            output.writeBits256(message.sharedSecret)
        }

        override fun decode(input: Input): AdnlProxyTo {
            val ip = input.readIntLittleEndian()
            val port = input.readIntLittleEndian()
            val date = input.readIntLittleEndian()
            val dateHash = input.readBits256()
            val sharedSecret = input.readBits256()
            return AdnlProxyTo(ip, port, date, dateHash, sharedSecret)
        }
    }
}