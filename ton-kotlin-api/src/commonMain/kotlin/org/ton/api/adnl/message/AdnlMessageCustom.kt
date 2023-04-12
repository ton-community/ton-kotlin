package org.ton.api.adnl.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.tl.ByteString
import org.ton.tl.TlConstructor
import org.ton.tl.TlReader
import org.ton.tl.TlWriter
import org.ton.tl.constructors.BytesTlConstructor
import kotlin.jvm.JvmName

@SerialName("adnl.message.custom")
@Serializable
public data class AdnlMessageCustom(
    @get:JvmName("data")
    val data: ByteString
) : AdnlMessage {
    public companion object : TlConstructor<AdnlMessageCustom>(
        schema = "adnl.message.custom data:bytes = adnl.Message",
    ) {
        public fun sizeOf(value: AdnlMessageCustom): Int =
            BytesTlConstructor.sizeOf(value.data)

        override fun decode(reader: TlReader): AdnlMessageCustom {
            val data = reader.readByteString()
            return AdnlMessageCustom(data)
        }

        override fun encode(writer: TlWriter, value: AdnlMessageCustom) {
            writer.writeBytes(value.data)
        }
    }
}
