package org.ton.crypto

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.serialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

private val DIGITS = "0123456789abcdef".toCharArray()

/**
 * Encode [bytes] as a HEX string with no spaces, newlines and `0x` prefixes.
 */
public fun hex(bytes: ByteArray): String = buildString(bytes.size * 2) {
    bytes.forEach { byte ->
        val b = byte.toInt() and 0xFF
        append(DIGITS[b shr 4])
        append(DIGITS[b and 0x0F])
    }
}

public fun hex(bytes: Iterable<Byte>): String = buildString {
    bytes.forEach { byte ->
        val b = byte.toInt() and 0xFF
        append(DIGITS[b shr 4])
        append(DIGITS[b and 0x0F])
    }
}

public fun hex(vararg longs: Long): String = buildString {
    longs.forEach {
        append(it.toString(16))
    }
}

/**
 * Decode bytes from HEX string. It should be no spaces and `0x` prefixes.
 */
public fun hex(s: String): ByteArray {
    val result = ByteArray(s.length / 2)
    for (idx in result.indices) {
        val srcIdx = idx * 2
        val high = s[srcIdx].toString().toInt(16) shl 4
        val low = s[srcIdx + 1].toString().toInt(16)
        result[idx] = (high or low).toByte()
    }
    return result
}

@Suppress("NOTHING_TO_INLINE")
public inline fun String.decodeHex(): ByteArray = hex(this)

@Suppress("NOTHING_TO_INLINE")
public inline fun ByteArray.encodeHex(): String = hex(this)

public object HexByteArraySerializer : KSerializer<ByteArray> {
    override val descriptor: SerialDescriptor = serialDescriptor<ByteArray>()

    override fun deserialize(decoder: Decoder): ByteArray = hex(decoder.decodeString())

    override fun serialize(encoder: Encoder, value: ByteArray) {
        encoder.encodeString(hex(value))
    }
}
