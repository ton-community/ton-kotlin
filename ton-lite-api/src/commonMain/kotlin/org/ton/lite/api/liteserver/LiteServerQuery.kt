@file:UseSerializers(HexByteArraySerializer::class)

package org.ton.lite.api.liteserver

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.ton.crypto.Base64ByteArraySerializer
import org.ton.crypto.HexByteArraySerializer
import org.ton.crypto.base64
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readBytesTl
import org.ton.tl.constructors.writeBytesTl

@Serializable
data class LiteServerQuery(
        @Serializable(Base64ByteArraySerializer::class)
        val data: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LiteServerQuery

        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int = data.contentHashCode()

    override fun toString(): String = buildString {
        append("LiteServerQuery(data=")
        append(base64(data))
        append(")")
    }

    companion object : TlConstructor<LiteServerQuery>(
            type = LiteServerQuery::class,
            schema = "liteServer.query data:bytes = Object"
    ) {
        override fun decode(input: Input): LiteServerQuery {
            val data = input.readBytesTl()
            return LiteServerQuery(data)
        }

        override fun encode(output: Output, value: LiteServerQuery) {
            output.writeBytesTl(value.data)
        }
    }
}