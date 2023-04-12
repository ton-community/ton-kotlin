package org.ton.api.adnl

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.tl.ByteString
import org.ton.tl.ByteString.Companion.toByteString
import org.ton.tl.TlConstructor
import org.ton.tl.TlReader
import org.ton.tl.TlWriter
import kotlin.jvm.JvmName

@Serializable
@SerialName("adnl.proxyToFastHash")
public data class AdnlProxyTo(
    @get:JvmName("ip")
    val ip: Int,

    @get:JvmName("port")
    val port: Int,

    @get:JvmName("date")
    val date: Int,

    @SerialName("date_hash")
    @get:JvmName("dateHash")
    val dateHash: ByteString,

    @SerialName("shared_secret")
    @get:JvmName("sharedSecret")
    val sharedSecret: ByteString
) {
    public constructor(
        ip: Int,
        port: Int,
        date: Int,
        dateHash: ByteArray,
        sharedSecret: ByteArray
    ) : this(ip, port, date, dateHash.toByteString(), sharedSecret.toByteString())

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
