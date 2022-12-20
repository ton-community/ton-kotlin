package org.ton.api.adnl

import kotlinx.serialization.Serializable
import org.ton.tl.TlConstructor
import org.ton.tl.TlReader
import org.ton.tl.TlWriter

@Serializable
public data class AdnlPing(
    val value: Long
) {
    public companion object : TlConstructor<AdnlPing>(
        "adnl.ping value:long = adnl.Pong"
    ) {
        override fun decode(reader: TlReader): AdnlPing {
            val value = reader.readLong()
            return AdnlPing(value)
        }

        override fun encode(writer: TlWriter, value: AdnlPing) {
            writer.writeLong(value.value)
        }
    }
}
