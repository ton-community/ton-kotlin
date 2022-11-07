@file:Suppress("OPT_IN_USAGE")

package org.ton.api.dht.config

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import org.ton.api.adnl.AdnlIdShort
import org.ton.tl.TlConstructor
import org.ton.tl.readTl
import org.ton.tl.writeTl

@SerialName("dht.config.local")
data class DhtConfigIdLocal(
    val id: AdnlIdShort
) : DhtConfigLocal {

    companion object : TlConstructor<DhtConfigIdLocal>(
        type = DhtConfigIdLocal::class,
        schema = "dht.config.local id:adnl.id_short = dht.config.Local"
    ) {
        override fun encode(output: Output, value: DhtConfigIdLocal) {
            output.writeTl(AdnlIdShort, value.id)
        }

        override fun decode(input: Input): DhtConfigIdLocal {
            val id = input.readTl(AdnlIdShort)
            return DhtConfigIdLocal(id)
        }
    }
}
