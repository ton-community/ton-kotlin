package org.ton.api.adnl.message

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import org.ton.api.JSON
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.IntTlConstructor
import org.ton.tl.constructors.writeIntTl

@SerialName("adnl.message.reinit")
@Serializable
data class AdnlMessageReinit(
    val date: Int
) : AdnlMessage {

    override fun toString(): String = JSON.encodeToString(this)

    companion object : TlConstructor<AdnlMessageReinit>(
        type = AdnlMessageReinit::class,
        schema = "adnl.message.reinit date:int = adnl.Message",
        fields = listOf(IntTlConstructor)
    ) {
        const val SIZE_BYTES = IntTlConstructor.SIZE_BYTES

        override fun encode(output: Output, value: AdnlMessageReinit) {
            output.writeIntTl(value.date)
        }

        override fun decode(values: Iterator<*>): AdnlMessageReinit {
            val date = values.next() as Int
            return AdnlMessageReinit(date)
        }
    }
}
