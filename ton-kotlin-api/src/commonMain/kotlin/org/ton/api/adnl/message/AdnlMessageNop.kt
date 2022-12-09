package org.ton.api.adnl.message

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import org.ton.api.JSON
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor


@SerialName("adnl.message.nop")
object AdnlMessageNop : AdnlMessage, TlConstructor<AdnlMessageNop>(
    schema = "adnl.message.nop = adnl.Message"
) {
    const val SIZE_BYTES: Int = 0

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AdnlMessageNop) return false
        return true
    }

    override fun decode(input: Input): AdnlMessageNop = this

    override fun encode(output: Output, value: AdnlMessageNop) {
    }
}
