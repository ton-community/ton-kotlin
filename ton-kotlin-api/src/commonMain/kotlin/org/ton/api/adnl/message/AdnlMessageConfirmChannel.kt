package org.ton.api.adnl.message

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.crypto.base64.Base64ByteArraySerializer
import org.ton.crypto.base64.base64
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readInt256Tl
import org.ton.tl.constructors.readIntTl
import org.ton.tl.constructors.writeInt256Tl
import org.ton.tl.constructors.writeIntTl

@SerialName("adnl.message.confirmChannel")
@Serializable
data class AdnlMessageConfirmChannel(
    @Serializable(Base64ByteArraySerializer::class)
    val key: ByteArray,
    @SerialName("peer_key")
    @Serializable(Base64ByteArraySerializer::class)
    val peerKey: ByteArray,
    val date: Int
) : AdnlMessage {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AdnlMessageConfirmChannel

        if (!key.contentEquals(other.key)) return false
        if (!peerKey.contentEquals(other.peerKey)) return false
        if (date != other.date) return false

        return true
    }

    override fun hashCode(): Int {
        var result = key.contentHashCode()
        result = 31 * result + peerKey.contentHashCode()
        result = 31 * result + date
        return result
    }

    override fun toString(): String = buildString {
        append("AdnlMessageConfirmChannel(key=")
        append(base64(key))
        append(", peerKey=")
        append(base64(peerKey))
        append(", date=")
        append(date)
        append(")")
    }

    companion object : TlConstructor<AdnlMessageConfirmChannel>(
        type = AdnlMessageConfirmChannel::class,
        schema = "adnl.message.confirmChannel key:int256 peer_key:int256 date:int = adnl.Message"
    ) {
        override fun encode(output: Output, value: AdnlMessageConfirmChannel) {
            output.writeInt256Tl(value.key)
            output.writeInt256Tl(value.peerKey)
            output.writeIntTl(value.date)
        }

        override fun decode(input: Input): AdnlMessageConfirmChannel {
            val key = input.readInt256Tl()
            val peerKey = input.readInt256Tl()
            val date = input.readIntTl()
            return AdnlMessageConfirmChannel(key, peerKey, date)
        }
    }
}
