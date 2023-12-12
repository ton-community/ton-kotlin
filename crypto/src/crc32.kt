package org.ton.crypto

// IEEE is by far and away the most common CRC-32 polynomial.
// Used by ethernet (IEEE 802.3), v.42, fddi, gzip, zip, png, ...
internal val IEEE_TABLE by lazy {
    crc32slicingTable(0xedb88320.toInt())
}

// Castagnoli's polynomial, used in iSCSI.
// Has better error detection characteristics than IEEE.
// https://dx.doi.org/10.1109/26.231911
internal val CASTAGNOLI_TABLE by lazy {
    crc32slicingTable(0x82f63b78.toInt())
}

@Deprecated("Use kotlinx-crypto instead")
public expect fun crc32(bytes: ByteArray, fromIndex: Int = 0, toIndex: Int = bytes.size - fromIndex): Int

@Deprecated("Use kotlinx-crypto instead")
public expect fun crc32c(bytes: ByteArray, fromIndex: Int = 0, toIndex: Int = bytes.size - fromIndex): Int

private fun crc32table(poly: Int, table: IntArray = IntArray(256)): IntArray {
    for (i in 0 until 256) {
        var crc = i
        for (j in 0 until 8) {
            crc = if (crc and 1 != 0) {
                (crc ushr 1) xor poly
            } else {
                crc ushr 1
            }
        }
        table[i] = crc
    }
    return table
}

private fun crc32slicingTable(poly: Int): Array<IntArray> {
    val table = Array(8) { IntArray(256) }
    crc32table(poly, table[0])
    for (i in 0 until 256) {
        var crc = table[0][i]
        for (j in 1 until 8) {
            crc = table[0][crc and 0xff] xor (crc ushr 8)
            table[j][i] = crc
        }
    }
    return table
}

internal inline fun crc32update(
    crc: Int,
    table: IntArray,
    bytes: ByteArray,
    startIndex: Int = 0,
    endIndex: Int = bytes.size - startIndex
): Int {
    var crc = crc.inv()
    for (i in startIndex until endIndex) {
        crc = table[(crc xor bytes[i].toInt()) and 0xff] xor (crc ushr 8)
    }
    return crc.inv()
}

internal inline fun crc32slicingUpdate(
    crc: Int,
    table: Array<IntArray>,
    bytes: ByteArray,
    fromIndex: Int = 0,
    toIndex: Int = bytes.size - fromIndex
): Int {
    var crc = crc
    var i = fromIndex
    if (toIndex - fromIndex >= 16) {
        crc = crc.inv()
        val end = fromIndex + toIndex
        val alignedEnd = end and -8
        while (i < alignedEnd) {
            val b0 = bytes[i++].toInt()
            val b1 = bytes[i++].toInt()
            val b2 = bytes[i++].toInt()
            val b3 = bytes[i++].toInt()
            val b4 = bytes[i++].toInt()
            val b5 = bytes[i++].toInt()
            val b6 = bytes[i++].toInt()
            val b7 = bytes[i++].toInt()
            crc = crc xor (b0 or (b1 shl 8) or (b2 shl 16) or (b3 shl 24))
            crc = table[0][b7] xor table[1][b6] xor table[2][b5] xor table[3][b4] xor
                    table[4][crc ushr 24 and 0xFF] xor table[5][crc ushr 16 and 0xFF] xor
                    table[6][crc ushr 8 and 0xFF] xor table[7][crc and 0xFF]
        }
        crc = crc.inv()
    }
    if (i == toIndex) return crc
    return crc32update(crc, table[0], bytes, i, toIndex)
}
