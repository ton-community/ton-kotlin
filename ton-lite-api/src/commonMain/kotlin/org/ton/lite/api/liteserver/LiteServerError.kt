package org.ton.lite.api.liteserver

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.tl.TlConstructor

@Serializable
data class LiteServerError(
        val code: Int,
        override val message: String
) : RuntimeException("$code $message") {
    override fun fillInStackTrace(): Throwable? = null

    companion object : TlConstructor<LiteServerError>(
            type = LiteServerError::class,
            schema = "liteServer.error code:int message:string = liteServer.Error"
    ) {
        override fun decode(input: Input): LiteServerError {
            val code = input.readIntLittleEndian()
            val message = input.readString()
            return LiteServerError(code, message)
        }

        override fun encode(output: Output, message: LiteServerError) {
            output.writeIntLittleEndian(message.code)
            output.writeString(message.message)
        }
    }
}