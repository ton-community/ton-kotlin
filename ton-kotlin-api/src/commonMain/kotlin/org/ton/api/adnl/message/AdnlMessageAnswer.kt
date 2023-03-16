package org.ton.api.adnl.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.bitstring.toBitString
import org.ton.tl.TlConstructor
import org.ton.tl.TlReader
import org.ton.tl.TlWriter
import org.ton.tl.constructors.BytesTlConstructor
import org.ton.tl.invoke

@SerialName("adnl.message.answer")
@Serializable
public data class AdnlMessageAnswer(
    @SerialName("query_id")
    val queryId: BitString,
    val answer: ByteArray
) : AdnlMessage {
    public constructor(queryId: ByteArray, answer: ByteArray) : this(queryId.toBitString(), answer)

    init {
        require(queryId.size == 256)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AdnlMessageAnswer) return false
        if (queryId != other.queryId) return false
        if (!answer.contentEquals(other.answer)) return false
        return true
    }

    override fun hashCode(): Int {
        var result = queryId.hashCode()
        result = 31 * result + answer.contentHashCode()
        return result
    }

    public companion object : TlConstructor<AdnlMessageAnswer>(
        schema = "adnl.message.answer query_id:int256 answer:bytes = adnl.Message",
    ) {
        public fun sizeOf(value: AdnlMessageAnswer): Int =
            256 / 8 + BytesTlConstructor.sizeOf(value.answer)

        override fun encode(writer: TlWriter, value: AdnlMessageAnswer): Unit = writer {
            writeRaw(value.queryId.toByteArray())
            writeBytes(value.answer)
        }

        override fun decode(reader: TlReader): AdnlMessageAnswer = reader {
            val queryId = readRaw(32)
            val answer = readBytes()
            AdnlMessageAnswer(queryId, answer)
        }
    }
}
