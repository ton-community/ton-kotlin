package org.ton.api.adnl.message

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.crypto.Base64ByteArraySerializer
import org.ton.crypto.base64
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readBytesTl
import org.ton.tl.constructors.writeBytesTl


@SerialName("adnl.message.custom")
@Serializable
data class AdnlMessageCustom(
    @Serializable(Base64ByteArraySerializer::class)
    val data: ByteArray
) : AdnlMessage {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AdnlMessageCustom

        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        return data.contentHashCode()
    }

    override fun toString(): String = buildString {
        append("AdnlMessageCustom(data=")
        append(base64(data))
        append(")")
    }

    companion object : TlConstructor<AdnlMessageCustom>(
        type = AdnlMessageCustom::class,
        schema = "adnl.message.custom data:bytes = adnl.Message"
    ) {
        override fun encode(output: Output, value: AdnlMessageCustom) {
            output.writeBytesTl(value.data)
        }

        override fun decode(input: Input): AdnlMessageCustom {
            val data = input.readBytesTl()
            return AdnlMessageCustom(data)
        }
    }
}
