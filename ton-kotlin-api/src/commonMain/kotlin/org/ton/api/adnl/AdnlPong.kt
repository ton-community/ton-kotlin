package org.ton.api.adnl

import kotlinx.serialization.Serializable
import org.ton.tl.TlConstructor
import org.ton.tl.TlReader
import org.ton.tl.TlWriter

@Serializable
public data class AdnlPong(
    val value: Long
) {
    public companion object : TlConstructor<AdnlPong>(
        schema = "adnl.pong value:long = adnl.Pong"
    ) {
        override fun decode(reader: TlReader): AdnlPong {
            val value = reader.readLong()
            return AdnlPong(value)
        }

        override fun encode(output: TlWriter, value: AdnlPong) {
            output.writeLong(value.value)
        }
    }
}
