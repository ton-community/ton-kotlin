package org.ton.api.adnl

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.crypto.Base64ByteArraySerializer
import org.ton.crypto.base64
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readInt256Tl
import org.ton.tl.constructors.readIntTl
import org.ton.tl.constructors.writeInt256Tl
import org.ton.tl.constructors.writeIntTl

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
        override fun encode(output: Output, value: AdnlProxyTo) {
            output.writeIntTl(value.ip)
            output.writeIntTl(value.port)
            output.writeIntTl(value.date)
            output.writeInt256Tl(value.dateHash)
            output.writeInt256Tl(value.sharedSecret)
        }

        override fun decode(input: Input): AdnlProxyTo {
            val ip = input.readIntTl()
            val port = input.readIntTl()
            val date = input.readIntTl()
            val dateHash = input.readInt256Tl()
            val sharedSecret = input.readInt256Tl()
            return AdnlProxyTo(ip, port, date, dateHash, sharedSecret)
        }
    }
}