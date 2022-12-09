package org.ton.api.adnl.message

import io.ktor.utils.io.core.*
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import org.ton.api.JSON
import org.ton.bitstring.BitString
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.*

@SerialName("adnl.message.createChannel")
@Serializable
data class AdnlMessageCreateChannel(
    val key: BitString,
    val date: Int
) : AdnlMessage {
    constructor(
        key: BitString,
        date: Instant
    ) : this(key, date.epochSeconds.toUInt().toInt())

    init {
        require(key.size == 256) { "Invalid key size. expected: 256, actual: ${key.size}" }
    }

    fun date() = Instant.fromEpochSeconds(date.toUInt().toLong())

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

    override fun toString(): String = JSON.encodeToString(this)

    companion object : TlConstructor<AdnlMessageCreateChannel>(
        schema = "adnl.message.createChannel key:int256 date:int = adnl.Message",
    ) {
        const val SIZE_BYTES = Int256TlConstructor.SIZE_BYTES + IntTlConstructor.SIZE_BYTES

        override fun encode(output: Output, value: AdnlMessageCreateChannel) {
            output.writeInt256Tl(value.key.toByteArray())
            output.writeIntTl(value.date)
        }

        override fun decode(input: Input): AdnlMessageCreateChannel {
            val key = BitString(input.readInt256Tl())
            val date = input.readIntTl()
            return AdnlMessageCreateChannel(key, date)
        }
    }
}
