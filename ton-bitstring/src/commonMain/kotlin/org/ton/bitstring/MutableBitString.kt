package org.ton.bitstring

interface MutableBitString : BitString, MutableList<Boolean> {
    operator fun set(index: Int, bit: Int)
    override operator fun set(index: Int, element: Boolean): Boolean

    override fun subList(fromIndex: Int, toIndex: Int): MutableBitString
}
