package org.ton.lite.api.liteserver

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readIntTl
import org.ton.tl.constructors.readStringTl
import org.ton.tl.constructors.writeIntTl
import org.ton.tl.constructors.writeStringTl

@Serializable
data class LiteServerError(
    val code: Int,
    override val message: String
) : RuntimeException("[$code] $message") {
    override fun toString(): String = "[$code] $message"

    companion object : TlConstructor<LiteServerError>(
        type = LiteServerError::class,
        schema = "liteServer.error code:int message:string = liteServer.Error"
    ) {
        override fun decode(input: Input): LiteServerError {
            val code = input.readIntTl()
            val message = input.readStringTl()
            return LiteServerError(code, message)
        }

        override fun encode(output: Output, value: LiteServerError) {
            output.writeIntTl(value.code)
            output.writeStringTl(value.message)
        }
    }
}