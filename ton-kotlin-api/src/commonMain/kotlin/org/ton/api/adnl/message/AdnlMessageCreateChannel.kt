package org.ton.api.adnl.message

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.crypto.Base64ByteArraySerializer
import org.ton.crypto.base64
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readInt256Tl
import org.ton.tl.constructors.readIntTl
import org.ton.tl.constructors.writeInt256Tl
import org.ton.tl.constructors.writeIntTl


@SerialName("adnl.message.createChannel")
@Serializable
data class AdnlMessageCreateChannel(
    @Serializable(Base64ByteArraySerializer::class)
    val key: ByteArray,
    val date: Int
) : AdnlMessage {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AdnlMessageCreateChannel

        if (!key.contentEquals(other.key)) return false
        if (date != other.date) return false

        return true
    }

    override fun hashCode(): Int {
        var result = key.contentHashCode()
        result = 31 * result + date
        return result
    }

    override fun toString(): String = buildString {
        append("AdnlMessageCreateChannel(key=")
        append(base64(key))
        append(", date=")
        append(date)
        append(")")
    }

    companion object : TlConstructor<AdnlMessageCreateChannel>(
        type = AdnlMessageCreateChannel::class,
        schema = "adnl.message.createChannel key:int256 date:int = adnl.Message"
    ) {
        override fun encode(output: Output, value: AdnlMessageCreateChannel) {
            output.writeInt256Tl(value.key)
            output.writeIntTl(value.date)
        }

        override fun decode(input: Input): AdnlMessageCreateChannel {
            val key = input.readInt256Tl()
            val date = input.readIntTl()
            return AdnlMessageCreateChannel(key, date)
        }
    }
}