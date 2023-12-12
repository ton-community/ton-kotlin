package org.ton.api.adnl.message

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.tl.ByteString
import org.ton.tl.ByteString.Companion.toByteString
import org.ton.tl.TlConstructor
import org.ton.tl.TlReader
import org.ton.tl.TlWriter
import kotlin.jvm.JvmName

@SerialName("adnl.message.createChannel")
@Serializable
public data class AdnlMessageCreateChannel(
    @get:JvmName("key")
    val key: ByteString,

    @get:JvmName("date")
    val date: Int
) : AdnlMessage {
    public constructor(
        key: ByteArray,
        date: Instant
    ) : this(key.toByteString(), date.epochSeconds.toUInt().toInt())

    public fun date(): Instant = Instant.fromEpochSeconds(date.toUInt().toLong())

    public companion object : TlConstructor<AdnlMessageCreateChannel>(
        schema = "adnl.message.createChannel key:int256 date:int = adnl.Message",
    ) {
        public const val SIZE_BYTES: Int = 256 / Byte.SIZE_BYTES + Int.SIZE_BYTES

        override fun encode(output: TlWriter, value: AdnlMessageCreateChannel) {
            output.writeRaw(value.key)
            output.writeInt(value.date)
        }

        override fun decode(input: TlReader): AdnlMessageCreateChannel {
            val key = input.readByteString(32)
            val date = input.readInt()
            return AdnlMessageCreateChannel(key, date)
        }
    }
}
