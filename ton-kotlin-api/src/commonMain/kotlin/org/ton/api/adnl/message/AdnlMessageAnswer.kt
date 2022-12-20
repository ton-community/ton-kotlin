package org.ton.api.adnl.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.tl.*
import org.ton.tl.constructors.*

@SerialName("adnl.message.answer")
@Serializable
public data class AdnlMessageAnswer(
    val query_id: Bits256,
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

    public companion object : TlConstructor<AdnlMessageAnswer>(
        schema = "adnl.message.answer query_id:int256 answer:bytes = adnl.Message",
    ) {
        public fun sizeOf(value: AdnlMessageAnswer): Int =
            256 / 8 + BytesTlConstructor.sizeOf(value.answer)

        override fun encode(writer: TlWriter, value: AdnlMessageAnswer): Unit = writer {
            writeBits256(value.query_id)
            writeBytes(value.answer)
        }

        override fun decode(reader: TlReader): AdnlMessageAnswer = reader {
            val queryId = readBits256()
            val answer = readBytes()
            AdnlMessageAnswer(queryId, answer)
        }
    }
}
