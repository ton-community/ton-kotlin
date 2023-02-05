package org.ton.api.adnl.message

import io.ktor.utils.io.core.*
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.bitstring.Bits256
import org.ton.tl.TlConstructor
import org.ton.tl.TlReader
import org.ton.tl.TlWriter
import org.ton.tl.constructors.*

@SerialName("adnl.message.createChannel")
@Serializable
public data class AdnlMessageCreateChannel(
    val key: Bits256,
    val date: Int
) : AdnlMessage {
    public constructor(
        key: BitString,
        date: Instant
    ) : this(Bits256(key), date.epochSeconds.toUInt().toInt())

    public fun date(): Instant = Instant.fromEpochSeconds(date.toUInt().toLong())

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AdnlMessageCreateChannel) return false
        if (key != other.key) return false
        if (date != other.date) return false
        return true
    }

    override fun hashCode(): Int {
        var result = key.hashCode()
        result = 31 * result + date
        return result
    }

    public companion object : TlConstructor<AdnlMessageCreateChannel>(
        schema = "adnl.message.createChannel key:int256 date:int = adnl.Message",
    ) {
        public const val SIZE_BYTES: Int = 256 / Byte.SIZE_BYTES + Int.SIZE_BYTES

        override fun encode(output: TlWriter, value: AdnlMessageCreateChannel) {
            output.writeBits256(value.key)
            output.writeInt(value.date)
        }

        override fun decode(input: TlReader): AdnlMessageCreateChannel {
            val key = input.readBits256()
            val date = input.readInt()
            return AdnlMessageCreateChannel(key, date)
        }
    }
}
