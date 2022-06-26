package org.ton.api.adnl

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readLongTl
import org.ton.tl.constructors.writeLongTl

@Serializable
data class AdnlPing(
    val value: Long
) {
    companion object : TlConstructor<AdnlPing>(
        AdnlPing::class,
        "adnl.ping value:long = adnl.Pong"
    ) {
        override fun decode(input: Input): AdnlPing {
            val value = input.readLongTl()
            return AdnlPing(value)
        }

        override fun encode(output: Output, value: AdnlPing) {
            output.writeLongTl(value.value)
        }
    }
}
