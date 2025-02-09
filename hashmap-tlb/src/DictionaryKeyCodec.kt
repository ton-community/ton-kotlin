@file:Suppress("PackageDirectoryMismatch")

package org.ton.kotlin.dict

import org.ton.bitstring.BitString

public interface DictionaryKeyCodec<K> : DictionaryKeyLoader<K>, DictionaryKeyStorer<K> {
    public companion object {
        public val INT32: DictionaryKeyCodec<Int> = object : DictionaryKeyCodec<Int> {
            override val keySize: Int get() = Int.SIZE_BITS

            override fun decodeKey(value: BitString): Int {
                require(value.size == Int.SIZE_BITS)
                return value.toHexString().toUInt(16).toInt()
            }

            override fun encodeKey(value: Int): BitString {
                return BitString.parse(value.toHexString())
            }
        }

        public val BITS256: DictionaryKeyCodec<BitString> = object : DictionaryKeyCodec<BitString> {
            override val keySize: Int get() = 256

            override fun decodeKey(value: BitString): BitString {
                require(value.size == 256)
                return value
            }

            override fun encodeKey(value: BitString): BitString {
                require(value.size == 256)
                return value
            }
        }
    }
}

public interface DictionaryKeyLoader<out K> {
    public val keySize: Int

    public fun decodeKey(value: BitString): K
}

public interface DictionaryKeyStorer<in K> {
    public val keySize: Int

    public fun encodeKey(value: K): BitString
}
