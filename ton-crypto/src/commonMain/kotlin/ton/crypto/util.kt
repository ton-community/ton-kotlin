@file:Suppress("NOTHING_TO_INLINE")

package ton.crypto

internal inline fun ByteArray.toIntArray() = IntArray(size) { this[it].toInt() and 0xFF }
internal inline fun IntArray.toByteArray() = ByteArray(size) { this[it].toByte() }

internal inline fun gf() = LongArray(16)
internal inline fun gf(init: LongArray) = LongArray(16).also { init.copyInto(it) }
internal val gf0 = gf()
internal val gf1 = gf(longArrayOf(1))
