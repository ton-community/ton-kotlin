package org.ton.bitstring

import kotlinx.io.bytestring.ByteString
import kotlin.jvm.JvmStatic

public interface MutableBitString : BitString {
    public operator fun set(index: Int, bit: Int)
    public operator fun set(index: Int, element: Boolean): Boolean

    public fun setBitsAt(index: Int, value: BitString)
    public fun setBitsAt(index: Int, value: Iterable<Boolean>)
    public fun setBitsAt(index: Int, value: ByteArray, bitCount: Int)
    public fun setBitsAt(index: Int, value: ByteString, bitCount: Int)

    public companion object {
        @JvmStatic
        public fun of(size: Int): MutableBitString = ByteBackedMutableBitString.of(size)
    }
}
