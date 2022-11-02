package org.ton.api.adnl.message

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor


@SerialName("adnl.message.nop")
@Serializable
class AdnlMessageNop : AdnlMessage {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AdnlMessageNop) return false
        return true
    }

    override fun hashCode(): Int = Companion.hashCode()

    companion object : TlConstructor<AdnlMessageNop>(
        type = AdnlMessageNop::class,
        schema = "adnl.message.nop = adnl.Message"
    ) {
        override fun encode(output: Output, value: AdnlMessageNop) {
        }

        override fun decode(input: Input): AdnlMessageNop = AdnlMessageNop()
    }
}
