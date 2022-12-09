package org.ton.api.adnl

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readLongTl
import org.ton.tl.constructors.writeLongTl

@Serializable
data class AdnlPong(
    val value: Long
) {
    companion object : TlConstructor<AdnlPong>(
        schema = "adnl.pong value:long = adnl.Pong"
    ) {
        override fun decode(input: Input): AdnlPong {
            val value = input.readLongTl()
            return AdnlPong(value)
        }

        override fun encode(output: Output, value: AdnlPong) {
            output.writeLongTl(value.value)
        }
    }
}
