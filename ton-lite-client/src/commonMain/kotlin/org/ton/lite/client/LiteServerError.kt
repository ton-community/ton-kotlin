package org.ton.lite.client

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.adnl.TLCodec

@Serializable
data class LiteServerError(
    val code: Int,
    override val message: String
) : RuntimeException("$code $message") {
    override fun fillInStackTrace(): Throwable? = null

    companion object : TLCodec<LiteServerError> {
        override val id: Int = -1146494648

        override fun decode(input: Input): LiteServerError {
            val code = input.readIntLittleEndian()
            val message = input.readByteArray().decodeToString()
            return LiteServerError(code, message)
        }

        override fun encode(output: Output, message: LiteServerError) {
            output.writeIntLittleEndian(message.code)
            output.writeByteArray(message.message.encodeToByteArray())
        }
    }
}