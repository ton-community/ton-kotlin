@file:UseSerializers(HexByteArraySerializer::class)

package org.ton.lite.api

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.ton.crypto.HexByteArraySerializer
import org.ton.tl.TLCodec

@Serializable
data class LiteServerQuery(
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

    companion object : TLCodec<LiteServerQuery> {
        override val id: Int = 2039219935

        override fun decode(input: Input): LiteServerQuery {
            val data = input.readByteArray()
            return LiteServerQuery(data)
        }

        override fun encode(output: Output, message: LiteServerQuery) {
            output.writeByteArray(message.data)
        }
    }
}