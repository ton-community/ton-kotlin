package org.ton.api.adnl

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.bitstring.toBitString
import org.ton.tl.TlConstructor
import org.ton.tl.TlReader
import org.ton.tl.TlWriter

@Serializable
public data class AdnlProxyTo(
    val ip: Int,
    val port: Int,
    val date: Int,
    @SerialName("date_hash")
    val dateHash: BitString,
    @SerialName("shared_secret")
    val sharedSecret: BitString
) {
    public constructor(
        ip: Int,
        port: Int,
        date: Int,
        dateHash: ByteArray,
        sharedSecret: ByteArray
    ) : this(ip, port, date, (dateHash).toBitString(), (sharedSecret).toBitString())

    public companion object : TlConstructor<AdnlProxyTo>(
        schema = "adnl.proxyToFastHash ip:int port:int date:int data_hash:int256 shared_secret:int256 = adnl.ProxyTo"
    ) {
        override fun encode(writer: TlWriter, value: AdnlProxyTo) {
            writer.writeInt(value.ip)
            writer.writeInt(value.port)
            writer.writeInt(value.date)
            writer.writeRaw(value.dateHash.toByteArray())
            writer.writeRaw(value.sharedSecret.toByteArray())
        }

        override fun decode(reader: TlReader): AdnlProxyTo {
            val ip = reader.readInt()
            val port = reader.readInt()
            val date = reader.readInt()
            val dateHash = reader.readRaw(32)
            val sharedSecret = reader.readRaw(32)
            return AdnlProxyTo(ip, port, date, dateHash, sharedSecret)
        }
    }
}
