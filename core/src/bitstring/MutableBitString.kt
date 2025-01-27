package org.ton.bitstring

import kotlinx.io.bytestring.ByteString
import org.ton.bigint.BigInt
import kotlin.jvm.JvmStatic

public interface MutableBitString : BitString {
    public operator fun set(index: Int, bit: Int)
    public operator fun set(index: Int, element: Boolean): Boolean

    public fun setBigIntAt(index: Int, value: BigInt, bits: Int)
    public fun setUBigIntAt(index: Int, value: BigInt, bits: Int)

    public fun setBitsAt(index: Int, value: BitString)
    public fun setBitsAt(index: Int, value: Iterable<Boolean>)
    public fun setBitsAt(index: Int, value: ByteArray, bitCount: Int)
    public fun setBitsAt(index: Int, value: ByteString, bitCount: Int)

    public companion object {
        @JvmStatic
        public fun of(size: Int): MutableBitString = ByteBackedMutableBitString.of(size)
    }
}
