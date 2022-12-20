package org.ton.api.adnl.message

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.tl.*
import org.ton.tl.constructors.*
import kotlin.jvm.JvmStatic

@SerialName("adnl.message.part")
@Serializable
public data class AdnlMessagePart(
    val hash: Bits256,
    val total_size: Int,
    val offset: Int,
    val data: ByteArray
) : AdnlMessage {
    public constructor(
        hash: ByteArray,
        totalSize: Int,
        offset: Int,
        data: ByteArray,
    ) : this(Bits256(hash), totalSize, offset, data)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AdnlMessagePart) return false
        if (hash != other.hash) return false
        if (total_size != other.total_size) return false
        if (offset != other.offset) return false
        if (!data.contentEquals(other.data)) return false
        return true
    }

    override fun hashCode(): Int {
        var result = hash.hashCode()
        result = 31 * result + total_size
        result = 31 * result + offset
        result = 31 * result + data.contentHashCode()
        return result
    }

    public companion object : TlCodec<AdnlMessagePart> by AdnlMessagePartTlConstructor {
        @JvmStatic
        public fun tlConstructor(): TlConstructor<AdnlMessagePart> = AdnlMessagePartTlConstructor

        @JvmStatic
        public fun sizeOf(value: AdnlMessagePart): Int =
            256 / 8 +
                    Int.SIZE_BYTES +
                    Int.SIZE_BYTES +
                    BytesTlConstructor.sizeOf(value.data)

        @JvmStatic
        public fun build(message: AdnlMessage, maxSize: Int): List<AdnlMessagePart> {
            val data = message.toByteArray()
            val hash = message.hash()
            var offset = 0
            val parts = ArrayList<AdnlMessagePart>()
            while (offset < data.size) {
                val partSize = minOf(maxSize, data.size - offset)
                val part = AdnlMessagePart(
                    hash = hash,
                    totalSize = data.size,
                    offset = offset,
                    data = data.copyOfRange(offset, offset + partSize)
                )
                parts.add(part)
                offset += partSize
            }
            return parts
        }
    }
}

private object AdnlMessagePartTlConstructor : TlConstructor<AdnlMessagePart>(
    schema = "adnl.message.part hash:int256 total_size:int offset:int data:bytes = adnl.Message;",
) {
    override fun decode(reader: TlReader): AdnlMessagePart {
        val hash = reader.readBits256()
        val totalSize = reader.readInt()
        val offset = reader.readInt()
        val data = reader.readBytes()
        return AdnlMessagePart(hash, totalSize, offset, data)
    }

    override fun encode(writer: TlWriter, value: AdnlMessagePart) {
        writer.writeBits256(value.hash)
        writer.writeInt(value.total_size)
        writer.writeInt(value.offset)
        writer.writeBytes(value.data)
    }
}
