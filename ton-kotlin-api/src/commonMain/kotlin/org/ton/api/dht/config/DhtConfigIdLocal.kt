@file:Suppress("OPT_IN_USAGE")

package org.ton.api.dht.config

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import org.ton.api.adnl.AdnlIdShort
import org.ton.tl.*

@SerialName("dht.config.local")
public data class DhtConfigIdLocal(
    val id: AdnlIdShort
) : DhtConfigLocal {

    public companion object : TlConstructor<DhtConfigIdLocal>(
        schema = "dht.config.local id:adnl.id_short = dht.config.Local"
    ) {
        override fun encode(output: TlWriter, value: DhtConfigIdLocal) {
            output.write(AdnlIdShort, value.id)
        }

        override fun decode(input: TlReader): DhtConfigIdLocal {
            val id = input.read(AdnlIdShort)
            return DhtConfigIdLocal(id)
        }
    }
}
