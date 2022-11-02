package org.ton.api.adnl.message

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.crypto.Base64ByteArraySerializer
import org.ton.crypto.HexByteArraySerializer
import org.ton.crypto.base64
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.*

@SerialName("adnl.message.part")
@Serializable
data class AdnlMessagePart(
    @Serializable(HexByteArraySerializer::class)
    val hash: ByteArray,
    val total_size: Int,
    val offset: Int,
    @Serializable(HexByteArraySerializer::class)
    val data: ByteArray
) : AdnlMessage {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AdnlMessagePart) return false

        if (total_size != other.total_size) return false
        if (offset != other.offset) return false
        if (!hash.contentEquals(other.hash)) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = hash.contentHashCode()
        result = 31 * result + total_size
        result = 31 * result + offset
        result = 31 * result + data.contentHashCode()
        return result
    }

    override fun toString(): String = buildString {
        append("AdnlMessagePart(hash=")
        append(base64(hash))
        append(", totalSize=")
        append(total_size)
        append(", offset=")
        append(offset)
        append(", data=")
        append(base64(data))
        append(")")
    }

    companion object : TlConstructor<AdnlMessagePart>(
        AdnlMessagePart::class,
        "adnl.message.part hash:int256 total_size:int offset:int data:bytes = adnl.Message;"
    ) {
        override fun decode(
            input: Input
        ): AdnlMessagePart {
            val hash = input.readInt256Tl()
            val totalSize = input.readIntTl()
            val offset = input.readIntTl()
            val data = input.readBytesTl()
            return AdnlMessagePart(hash, totalSize, offset, data)
        }

        override fun encode(output: Output, value: AdnlMessagePart) {
            output.writeInt256Tl(value.hash)
            output.writeIntTl(value.total_size)
            output.writeIntTl(value.offset)
            output.writeBytesTl(value.data)
        }
    }
}
