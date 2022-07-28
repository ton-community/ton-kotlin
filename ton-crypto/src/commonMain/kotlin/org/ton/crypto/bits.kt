@file:Suppress("NOTHING_TO_INLINE")

package org.ton.crypto

import kotlin.experimental.or

private val rev8tab = hex(buildString {
    append("008040c020a060e0109050d030b070f0")
    append("088848c828a868e8189858d838b878f8")
    append("048444c424a464e4149454d434b474f4")
    append("0c8c4ccc2cac6cec1c9c5cdc3cbc7cfc")
    append("028242c222a262e2129252d232b272f2")
    append("0a8a4aca2aaa6aea1a9a5ada3aba7afa")
    append("068646c626a666e6169656d636b676f6")
    append("0e8e4ece2eae6eee1e9e5ede3ebe7efe")
    append("018141c121a161e1119151d131b171f1")
    append("098949c929a969e9199959d939b979f9")
    append("058545c525a565e5159555d535b575f5")
    append("0d8d4dcd2dad6ded1d9d5ddd3dbd7dfd")
    append("038343c323a363e3139353d333b373f3")
    append("0b8b4bcb2bab6beb1b9b5bdb3bbb7bfb")
    append("078747c727a767e7179757d737b777f7")
    append("0f8f4fcf2faf6fef1f9f5fdf3fbf7fff")
})

fun Byte.reverse() = rev8tab[this.toInt()]

fun Short.reverse() = rev8tab[this.toInt() shr 8].toShort() or (rev8tab[this.toInt() and 0xff].toInt() shl 8).toShort()

inline fun lowerBits64(x: ULong) = x and bitsNegative64(x)
inline fun bitsNegative64(x: ULong) = x.inv() + 1u
inline fun ULong.toBoolean() = this != 0uL
inline fun Long.toBoolean() = this != 0L
inline fun UInt.toBoolean() = this != 0u
inline fun Int.toBoolean() = this != 0
