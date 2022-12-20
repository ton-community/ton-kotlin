package org.ton.lite.api.liteserver

import kotlinx.serialization.Serializable
import org.ton.tl.TlConstructor
import org.ton.tl.TlReader
import org.ton.tl.TlWriter

@Serializable
public data class LiteServerError(
    val code: Int,
    val message: String
) {
    override fun toString(): String = "[$code] $message"

    public companion object : TlConstructor<LiteServerError>(
        schema = "liteServer.error code:int message:string = liteServer.Error"
    ) {
        override fun decode(reader: TlReader): LiteServerError {
            val code = reader.readInt()
            val message = reader.readString()
            return LiteServerError(code, message)
        }

        override fun encode(writer: TlWriter, value: LiteServerError) {
            writer.writeInt(value.code)
            writer.writeString(value.message)
        }
    }
}
