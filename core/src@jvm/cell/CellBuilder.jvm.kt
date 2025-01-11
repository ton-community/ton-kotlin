package org.ton.cell

public fun CellBuilder.storeInt(value: java.math.BigInteger, length: Int): CellBuilder = apply {
    val intBits = java.math.BigInteger.ONE shl (length - 1)
    require(value >= -intBits && value < intBits) { "Can't store an Int, because its value allocates more space than provided." }

    val bits = BooleanArray(length) { index ->
        ((value shr index) and java.math.BigInteger.ONE).toInt() != 0
    }
    bits.reverse()
    storeBits(*bits)
}

public fun CellBuilder.storeUInt(value: java.math.BigInteger, length: Int): CellBuilder {
    check(value.signum() >= 0) { "Integer `$value` must be unsigned" }
    return storeInt(value, length)
}