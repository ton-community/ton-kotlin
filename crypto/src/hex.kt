package org.ton.crypto

/**
 * Encode [bytes] as a HEX string with no spaces, newlines and `0x` prefixes.
 */
@OptIn(ExperimentalStdlibApi::class)
@Deprecated("Use kotlin stdlib instead", replaceWith = ReplaceWith("bytes.toHexString()"))
public fun hex(bytes: ByteArray): String = bytes.toHexString()

@OptIn(ExperimentalStdlibApi::class)
@Deprecated("Use kotlin stdlib instead", replaceWith = ReplaceWith("bytes.toList().toByteArray().toHexString()"))
public fun hex(bytes: Iterable<Byte>): String = bytes.toList().toByteArray().toHexString()

@OptIn(ExperimentalStdlibApi::class)
@Deprecated(
    "Use kotlin stdlib instead", replaceWith = ReplaceWith(
        "longs.asSequence().map {\n" +
                "        it.toHexString()\n" +
                "    }.joinToString(\"\")"
    )
)
public fun hex(vararg longs: Long): String = buildString {
    longs.asSequence().map {
        it.toHexString()
    }.joinToString("")
}

/**
 * Decode bytes from HEX string. It should be no spaces and `0x` prefixes.
 */
@OptIn(ExperimentalStdlibApi::class)
@Deprecated("Use kotlin stdlib instead", replaceWith = ReplaceWith("s.hexToByteArray()"))
public fun hex(s: String): ByteArray = s.hexToByteArray()

@OptIn(ExperimentalStdlibApi::class)
@Deprecated("Use kotlin stdlib instead", replaceWith = ReplaceWith("hexToByteArray()"))
public fun String.decodeHex(): ByteArray = hexToByteArray()

@OptIn(ExperimentalStdlibApi::class)
@Deprecated("Use kotlin stdlib instead", replaceWith = ReplaceWith("toHexString()"))
public fun ByteArray.encodeHex(): String = toHexString()
