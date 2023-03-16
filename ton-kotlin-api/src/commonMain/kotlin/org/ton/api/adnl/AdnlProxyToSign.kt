package org.ton.api.adnl

import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.bitstring.toBitString
import org.ton.tl.TlConstructor
import org.ton.tl.TlReader
import org.ton.tl.TlWriter

@Serializable
public data class AdnlProxyToSign(
    val ip: Int,
    val port: Int,
    val date: Int,
    val signature: BitString
) {
    public constructor(
        ip: Int,
        port: Int,
        date: Int,
        signature: ByteArray
    ) : this(ip, port, date, signature.toBitString())

    init {
        require(signature.size == 256)
    }

    public companion object : TlConstructor<AdnlProxyToSign>(
        schema = "adnl.proxyToFast ip:int port:int date:int signature:int256 = adnl.ProxyToSign"
    ) {
        override fun encode(writer: TlWriter, value: AdnlProxyToSign) {
            writer.writeInt(value.ip)
            writer.writeInt(value.port)
            writer.writeInt(value.date)
            writer.writeRaw(value.signature.toByteArray())
        }

        override fun decode(reader: TlReader): AdnlProxyToSign {
            val ip = reader.readInt()
            val port = reader.readInt()
            val date = reader.readInt()
            val signature = reader.readRaw(32)
            return AdnlProxyToSign(ip, port, date, signature)
        }
    }
}
