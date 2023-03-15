package org.ton.api.adnl.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.crypto.Base64ByteArraySerializer
import org.ton.tl.TlConstructor
import org.ton.tl.TlReader
import org.ton.tl.TlWriter
import org.ton.tl.constructors.BytesTlConstructor

@SerialName("adnl.message.custom")
@Serializable
public data class AdnlMessageCustom(
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

    public companion object : TlConstructor<AdnlMessageCustom>(
        schema = "adnl.message.custom data:bytes = adnl.Message",
    ) {
        public fun sizeOf(value: AdnlMessageCustom): Int =
            BytesTlConstructor.sizeOf(value.data)

        override fun decode(reader: TlReader): AdnlMessageCustom {
            val data = reader.readBytes()
            return AdnlMessageCustom(data)
        }

        override fun encode(writer: TlWriter, value: AdnlMessageCustom) {
            writer.writeBytes(value.data)
        }
    }
}
