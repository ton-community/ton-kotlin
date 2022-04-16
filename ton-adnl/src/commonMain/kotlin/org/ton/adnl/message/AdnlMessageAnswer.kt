package org.ton.adnl.message

import io.ktor.utils.io.core.*
import org.ton.adnl.TLCodec
import org.ton.crypto.hex

data class AdnlMessageAnswer(
    val queryId: ByteArray,
    val answer: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AdnlMessageAnswer

        if (!queryId.contentEquals(other.queryId)) return false
        if (!answer.contentEquals(other.answer)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = queryId.contentHashCode()
        result = 31 * result + answer.contentHashCode()
        return result
    }

    override fun toString() = "AdnlMessageAnswer(queryId=${hex(queryId)}, answer=${hex(answer)})"

    companion object : TLCodec<AdnlMessageAnswer> {
        override val id = 262964246

        override fun encode(output: Output, message: AdnlMessageAnswer) {
            output.writeFully(message.queryId)
            output.writeByteArray(message.answer)
        }

        override fun decode(input: Input): AdnlMessageAnswer {
            val queryId = input.readBytes(32)
            val answer = input.readByteArray()
            return AdnlMessageAnswer(queryId, answer)
        }
    }
}