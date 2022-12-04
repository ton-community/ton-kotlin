package org.ton.api.adnl.message

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import org.ton.api.JSON
import org.ton.crypto.base64.Base64ByteArraySerializer
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.BytesTlConstructor
import org.ton.tl.constructors.writeBytesTl


@SerialName("adnl.message.custom")
@Serializable
data class AdnlMessageCustom(
    @Serializable(Base64ByteArraySerializer::class)
    val data: ByteArray
) : AdnlMessage {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AdnlMessageCustom) return false
        if (!data.contentEquals(other.data)) return false
        return true
    }

    override fun hashCode(): Int {
        return data.contentHashCode()
    }

    override fun toString(): String = JSON.encodeToString(this)

    companion object : TlConstructor<AdnlMessageCustom>(
        type = AdnlMessageCustom::class,
        schema = "adnl.message.custom data:bytes = adnl.Message",
        fields = listOf(BytesTlConstructor)
    ) {
        fun sizeOf(value: AdnlMessageCustom): Int =
            BytesTlConstructor.sizeOf(value.data)

        override fun encode(output: Output, value: AdnlMessageCustom) {
            output.writeBytesTl(value.data)
        }

        override fun decode(values: Iterator<*>): AdnlMessageCustom {
            val data = values.next() as ByteArray
            return AdnlMessageCustom(data)
        }
    }
}
