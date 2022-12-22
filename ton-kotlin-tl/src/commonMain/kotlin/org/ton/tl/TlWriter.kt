package org.ton.tl

import io.ktor.utils.io.core.*
import org.ton.tl.constructors.Bool
import org.ton.tl.constructors.BoolTlCombinator

public class TlWriter(
    public val output: Output = BytePacketBuilder()
) {
    public fun writeBoolean(value: Boolean) {
        BoolTlCombinator.encode(this, Bool[value])
    }

    public fun writeInt(value: Int) {
        output.writeIntLittleEndian(value)
    }

    public fun writeLong(value: Long) {
        output.writeLongLittleEndian(value)
    }

    public fun writeBytes(value: ByteArray, offset: Int = 0, length: Int = value.size - offset) {
        var totalLength = length
        if (totalLength < 254) {
            output.writeUByte(totalLength.toUByte())
            totalLength++
        } else if (totalLength < (1 shl 24)) {
            output.writeUByte(254u)
            output.writeUByte((totalLength and 255).toUByte())
            output.writeUByte(((totalLength shr 8) and 255).toUByte())
            output.writeUByte((totalLength shr 16).toUByte())
            totalLength += 4
        } else if (totalLength < Int.MAX_VALUE) {
            output.writeUByte(255u)
            output.writeUByte((totalLength and 255).toUByte())
            output.writeUByte(((totalLength shr 8) and 255).toUByte())
            output.writeUByte(((totalLength shr 16) and 255).toUByte())
            output.writeUByte(((totalLength shr 24) and 255).toUByte())
            output.writeByte(0)
            output.writeByte(0)
            output.writeByte(0)
            totalLength += 8
        } else {
            throw IllegalStateException("Too big byte array: $totalLength")
        }
        output.writeFully(value, offset, length)
        while (totalLength++ % 4 > 0) {
            output.writeByte(0)
        }
    }

    public fun writeString(value: String) {
        writeBytes(value.encodeToByteArray())
    }

    public fun writeBits128(value: Bits128) {
        output.writeFully(value.toByteArray())
    }

    public fun writeBits256(value: Bits256) {
        output.writeFully(value.toByteArray())
    }

    public inline operator fun invoke(block: TlWriter.() -> Unit) {
        block()
    }
}

public inline fun <T> TlWriter.write(codec: TlCodec<T>, value: T) {
    codec.encode(this, value)
}

public inline fun <T : Any> TlWriter.writeNullable(flag: Int, index: Int, value: T?, block: TlWriter.(T) -> Unit) {
    writeNullable(flag and (1 shl index) != 0, value, block)
}

public inline fun <T : Any> TlWriter.writeNullable(flag: Boolean, value: T?, block: TlWriter.(T) -> Unit) {
    if (flag) {
        if (value != null) {
            block(value)
        } else {
            throw IllegalStateException("Nullable value is null, but flag is true")
        }
    }
}

public inline fun <T> TlWriter.writeCollection(value: Collection<T>, block: TlWriter.(T) -> Unit) {
    writeInt(value.size)
    for (item in value) {
        block(item)
    }
}
