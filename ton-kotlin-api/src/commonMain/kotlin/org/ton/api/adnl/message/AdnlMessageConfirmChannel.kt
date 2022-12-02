@file:Suppress("PropertyName")

package org.ton.api.adnl.message

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.crypto.base64.Base64ByteArraySerializer
import org.ton.crypto.base64.base64
import org.ton.tl.TlConstructor
import kotlinx.datetime.Instant
import kotlinx.serialization.encodeToString
import org.ton.api.JSON
import org.ton.bitstring.BitString
import org.ton.tl.constructors.*

@SerialName("adnl.message.confirmChannel")
@Serializable
data class AdnlMessageConfirmChannel(
    val key: BitString,
    val peer_key: BitString,
    val date: Int
) : AdnlMessage {
    constructor(
        key: BitString,
        peerKey: BitString,
        date: Instant
    ) : this(key, peerKey, date.epochSeconds.toInt())

    init {
        require(key.size == 256) { "Invalid key size. expected: 256, actual: ${key.size}" }
        require(peer_key.size == 256) { "Invalid peer_key size. expected: 256, actual: ${peer_key.size}" }
    }

    fun date(): Instant = Instant.fromEpochSeconds(date.toUInt().toLong())

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AdnlMessageConfirmChannel

        if (key != other.key) return false
        if (peer_key != other.peer_key) return false
        if (date != other.date) return false

        return true
    }

    override fun hashCode(): Int {
        var result = key.hashCode()
        result = 31 * result + peer_key.hashCode()
        result = 31 * result + date
        return result
    }

    override fun toString(): String = JSON.encodeToString(this)

    companion object : TlConstructor<AdnlMessageConfirmChannel>(
        type = AdnlMessageConfirmChannel::class,
        schema = "adnl.message.confirmChannel key:int256 peer_key:int256 date:int = adnl.Message",
        fields = listOf(Int256TlConstructor, Int256TlConstructor, IntTlConstructor)
    ) {
        const val SIZE_BYTES = Int256TlConstructor.SIZE_BYTES + Int256TlConstructor.SIZE_BYTES + IntTlConstructor.SIZE_BYTES

        override fun encode(output: Output, value: AdnlMessageConfirmChannel) {
            output.writeInt256Tl(value.key.toByteArray())
            output.writeInt256Tl(value.peer_key.toByteArray())
            output.writeIntTl(value.date)
        }

        override fun decode(values: Iterator<*>): AdnlMessageConfirmChannel {
            val key = values.next() as ByteArray
            val peerKey = values.next() as ByteArray
            val date = values.next() as Int
            return AdnlMessageConfirmChannel(BitString(key), BitString(peerKey), date)
        }
    }
}
