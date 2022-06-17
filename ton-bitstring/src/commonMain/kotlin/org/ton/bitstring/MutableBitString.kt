package org.ton.bitstring

interface MutableBitString : BitString, MutableList<Boolean> {
    operator fun set(index: Int, bit: Int)
    override operator fun set(index: Int, element: Boolean): Boolean

    override operator fun plus(bits: BooleanArray): MutableBitString
    override operator fun plus(bits: Iterable<Boolean>): MutableBitString
    override operator fun plus(bits: Collection<Boolean>): MutableBitString
    override operator fun plus(bytes: ByteArray): MutableBitString
    override fun plus(bytes: ByteArray, bits: Int): MutableBitString

    override fun subList(fromIndex: Int, toIndex: Int): MutableBitString
}
