package org.ton.bitstring

internal object EmptyBitString : BitString, List<Boolean> by emptyList() {
    override fun getOrNull(index: Int): Boolean? = null

    override fun plus(bits: BooleanArray): BitString = BitString(*bits)

    override fun plus(bits: Iterable<Boolean>): BitString = BitString(bits)

    override fun plus(bits: Collection<Boolean>): BitString = BitString(bits)

    override fun plus(bytes: ByteArray): BitString = BitString(bytes)

    override fun plus(bytes: ByteArray, bits: Int): BitString = BitString(bytes, bits)

    override fun subList(fromIndex: Int, toIndex: Int): BitString {
        return super.subList(fromIndex, toIndex)
    }

    override fun slice(indices: IntRange): BitString {
        if (indices.first == 0 && indices.last == 0) return this
        throw IndexOutOfBoundsException(indices.toString())
    }

    override fun toByteArray(): ByteArray = byteArrayOf()

    override fun toBooleanArray(): BooleanArray = booleanArrayOf()

    override fun toMutableBitString(): MutableBitString = ByteBackedMutableBitString(ByteArray(1), 0)

    override fun toString(): String = "_"

    override fun compareTo(other: BitString): Int = -1
}