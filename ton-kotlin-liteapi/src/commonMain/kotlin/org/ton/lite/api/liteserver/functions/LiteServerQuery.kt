@file:UseSerializers(HexByteArraySerializer::class)

package org.ton.lite.api.liteserver.functions

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.ton.crypto.HexByteArraySerializer
import org.ton.tl.*

@Serializable
@SerialName("liteServer.query")
public data class LiteServerQuery(
    val data: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LiteServerQuery) return false
        if (!data.contentEquals(other.data)) return false
        return true
    }

    override fun hashCode(): Int = data.contentHashCode()

    public companion object : TlCodec<LiteServerQuery> by LiteServerQueryTlConstructor
}

private object LiteServerQueryTlConstructor : TlConstructor<LiteServerQuery>(
    schema = "liteServer.query data:bytes = Object"
) {
    override fun decode(input: TlReader): LiteServerQuery {
        val data = input.readBytes()
        return LiteServerQuery(data)
    }

    override fun encode(output: TlWriter, value: LiteServerQuery) {
        output.writeBytes(value.data)
    }
}
