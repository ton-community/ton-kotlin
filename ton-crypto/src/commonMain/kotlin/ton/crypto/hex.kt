package ton.crypto

private val DIGITS = "0123456789abcdef".toCharArray()

/**
 * Encode [bytes] as a HEX string with no spaces, newlines and `0x` prefixes.
 */
fun hex(bytes: ByteArray): String {
    val result = CharArray(bytes.size * 2)
    var resultIndex = 0

    for (element in bytes) {
        val b = element.toInt() and 0xff
        result[resultIndex++] = DIGITS[b shr 4]
        result[resultIndex++] = DIGITS[b and 0x0f]
    }

    return result.concatToString()
}

fun hex(vararg longs: Long): String = buildString {
    longs.forEach {
        append(it.toString(16))
    }
}

fun hex(vararg uLongs: ULong): String = buildString {
    uLongs.forEach {
        append(it.toString(16))
    }
}

/**
 * Decode bytes from HEX string. It should be no spaces and `0x` prefixes.
 */
fun hex(s: String): ByteArray {
    val result = ByteArray(s.length / 2)
    for (idx in result.indices) {
        val srcIdx = idx * 2
        val high = s[srcIdx].toString().toInt(16) shl 4
        val low = s[srcIdx + 1].toString().toInt(16)
        result[idx] = (high or low).toByte()
    }
    return result
}