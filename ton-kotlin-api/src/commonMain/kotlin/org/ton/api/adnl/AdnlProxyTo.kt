package org.ton.api.adnl

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.Bits256
import org.ton.tl.TlConstructor
import org.ton.tl.TlReader
import org.ton.tl.TlWriter

@Serializable
public data class AdnlProxyTo(
    val ip: Int,
    val port: Int,
    val date: Int,
    @SerialName("date_hash")
    val dateHash: Bits256,
    @SerialName("shared_secret")
    val sharedSecret: Bits256
) {
    public constructor(
        ip: Int,
        port: Int,
        date: Int,
        dateHash: ByteArray,
        sharedSecret: ByteArray
    ) : this(ip, port, date, Bits256(dateHash), Bits256(sharedSecret))

    public companion object : TlConstructor<AdnlProxyTo>(
        schema = "adnl.proxyToFastHash ip:int port:int date:int data_hash:int256 shared_secret:int256 = adnl.ProxyTo"
    ) {
        override fun encode(writer: TlWriter, value: AdnlProxyTo) {
            writer.writeInt(value.ip)
            writer.writeInt(value.port)
            writer.writeInt(value.date)
            writer.writeBits256(value.dateHash)
            writer.writeBits256(value.sharedSecret)
        }

        override fun decode(reader: TlReader): AdnlProxyTo {
            val ip = reader.readInt()
            val port = reader.readInt()
            val date = reader.readInt()
            val dateHash = reader.readBits256()
            val sharedSecret = reader.readBits256()
            return AdnlProxyTo(ip, port, date, dateHash, sharedSecret)
        }
    }
}
