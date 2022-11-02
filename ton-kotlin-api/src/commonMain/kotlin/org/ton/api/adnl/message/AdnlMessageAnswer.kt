package org.ton.api.adnl.message

import io.ktor.util.*
import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.crypto.Base64ByteArraySerializer
import org.ton.crypto.HexByteArraySerializer
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.*

@SerialName("adnl.message.answer")
@Serializable
data class AdnlMessageAnswer(
    @Serializable(HexByteArraySerializer::class)
    val query_id: ByteArray,
    @Serializable(HexByteArraySerializer::class)
    val answer: ByteArray
) : AdnlMessage {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AdnlMessageAnswer

        if (!query_id.contentEquals(other.query_id)) return false
        if (!answer.contentEquals(other.answer)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = query_id.contentHashCode()
        result = 31 * result + answer.contentHashCode()
        return result
    }

    override fun toString() = buildString {
        append("AdnlMessageAnswer(queryId=")
        append(hex(query_id))
        append(", answer=")
        append(hex(answer))
        append(")")
    }

    companion object : TlConstructor<AdnlMessageAnswer>(
        type = AdnlMessageAnswer::class,
        schema = "adnl.message.answer query_id:int256 answer:bytes = adnl.Message",
        fields = listOf(Int256TlConstructor, BytesTlConstructor)
    ) {
        override fun encode(output: Output, value: AdnlMessageAnswer) {
            output.writeInt256Tl(value.query_id)
            output.writeBytesTl(value.answer)
        }

        override fun decode(input: Input): AdnlMessageAnswer {
            val queryId = input.readInt256Tl()
            val answer = input.readBytesTl()
            return AdnlMessageAnswer(queryId, answer)
        }
    }
}
