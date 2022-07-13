package org.ton.api.adnl.message

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.crypto.Base64ByteArraySerializer
import org.ton.crypto.base64
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readBytesTl
import org.ton.tl.constructors.readInt256Tl
import org.ton.tl.constructors.writeBytesTl
import org.ton.tl.constructors.writeInt256Tl


@SerialName("adnl.message.answer")
@Serializable
data class AdnlMessageAnswer(
    @SerialName("query_id")
    @Serializable(Base64ByteArraySerializer::class)
    val query_id: ByteArray,
    @Serializable(Base64ByteArraySerializer::class)
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
        append(base64(query_id))
        append(", answer=")
        append(base64(answer))
        append(")")
    }

    companion object : TlConstructor<AdnlMessageAnswer>(
        type = AdnlMessageAnswer::class,
        schema = "adnl.message.answer query_id:int256 answer:bytes = adnl.Message"
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
