package org.ton.dict

import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder

internal fun CellBuilder.storeLabel(
    maxKeyLength: Int,
    key: BitString,
    startIndex: Int,
    endIndex: Int
) {
    val length = endIndex - startIndex
    if (length == 0) {
        // mode 'hml_short$0', requires 2n+2 bits (always for n=0)
        bitsPosition += 2
        return
    }
    val k = Int.SIZE_BITS - maxKeyLength.countLeadingZeroBits()

    val firstBit = key[startIndex]
    when {
        length > 1 && k < 2 * length - 1 && length == key.countLeadingBits(startIndex, endIndex, firstBit) -> {
            // mode 'hml_same$11', requires 3+k bits (for n>=2, k<2n-1)
            storeUInt(if (firstBit) 0b111 else 0b110, 3)
            storeUInt(length, k)
            return
        }

        k < length -> {
            // mode 'hml_long$10', requires 2+k+n bits
            storeUInt(0b10, 2) // tag hml_long$10
            storeUInt(length, k) // 'n:(#<= m)' from hml_long
        }

        else -> {
            // mode 'hml_short$0', requires 1+2n+1 bits
            storeBit(false) // tag hml_short$0
            // 'len:(Unary ~n)' from hml_short,
            // where -0b10 = 0b1111_1111_1111_1110,
            // unary_succ$1 - counter for n
            // unary_zero$0 - terminator for counter
            // 1110 -> n=3 + 1 terminator bit
            storeLong(-0b10, length + 1)
        }
    }
    storeBitString(key, startIndex, endIndex)
}