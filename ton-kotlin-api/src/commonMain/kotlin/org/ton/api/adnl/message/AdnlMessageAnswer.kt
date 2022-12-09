package org.ton.api.adnl.message

import io.ktor.util.*
import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import org.ton.api.JSON
import org.ton.bitstring.BitString
import org.ton.crypto.HexByteArraySerializer
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.*

@SerialName("adnl.message.answer")
@Serializable
data class AdnlMessageAnswer(
    val query_id: BitString,
    @Serializable(HexByteArraySerializer::class)
    val answer: ByteArray
) : AdnlMessage {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AdnlMessageAnswer) return false
        if (query_id != other.query_id) return false
        if (!answer.contentEquals(other.answer)) return false
        return true
    }

    override fun hashCode(): Int {
        var result = query_id.hashCode()
        result = 31 * result + answer.contentHashCode()
        return result
    }

    override fun toString(): String = JSON.encodeToString(this)

    companion object : TlConstructor<AdnlMessageAnswer>(
        schema = "adnl.message.answer query_id:int256 answer:bytes = adnl.Message",
    ) {
        fun sizeOf(value: AdnlMessageAnswer): Int =
            Int256TlConstructor.SIZE_BYTES + BytesTlConstructor.sizeOf(value.answer)

        override fun encode(output: Output, value: AdnlMessageAnswer) {
            output.writeInt256Tl(value.query_id.toByteArray())
            output.writeBytesTl(value.answer)
        }

        override fun decode(input: Input): AdnlMessageAnswer {
            val queryId = input.readInt256Tl()
            val answer = input.readBytesTl()
            return AdnlMessageAnswer(BitString(queryId), answer)
        }
    }
}
