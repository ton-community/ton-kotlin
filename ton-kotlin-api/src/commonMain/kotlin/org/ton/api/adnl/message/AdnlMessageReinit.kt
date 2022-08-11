package org.ton.api.adnl.message

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readIntTl
import org.ton.tl.constructors.writeIntTl

@SerialName("adnl.message.reinit")
@Serializable
data class AdnlMessageReinit(
    val date: Int
) : AdnlMessage {
    companion object : TlConstructor<AdnlMessageReinit>(
        type = AdnlMessageReinit::class,
        schema = "adnl.message.reinit date:int = adnl.Message"
    ) {
        override fun encode(output: Output, value: AdnlMessageReinit) {
            output.writeIntTl(value.date)
        }

        override fun decode(input: Input): AdnlMessageReinit {
            val date = input.readIntTl()
            return AdnlMessageReinit(date)
        }
    }
}
