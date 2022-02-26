@file:JvmName("Utils")

package ton.common.biginteger

import kotlin.jvm.JvmName

internal fun Long.numberOfLeadingZeros(): Int {
    // HD, Figure 5-6
    if (this == 0L)
        return 64
    var n = 1
    var x = this.ushr(32).toInt()

    if (x == 0) {
        n += 32
        x = this.toInt()
    }
    if (x.ushr(16) == 0) {
        n += 16
        x = x shl 16
    }
    if (x.ushr(24) == 0) {
        n += 8
        x = x shl 8
    }
    if (x.ushr(28) == 0) {
        n += 4
        x = x shl 4
    }
    if (x.ushr(30) == 0) {
        n += 2
        x = x shl 2
    }
    n -= x.ushr(31)
    return n
}

internal fun Int.numberOfLeadingZeros(): Int {
    // HD, Figure 5-6
    var x = this

    if (x == 0)

        return 32

    var n = 1

    if (x.ushr(16) == 0) {
        n += 16
        x = x shl 16
    }

    if (x.ushr(24) == 0) {
        n += 8
        x = x shl 8
    }

    if (x.ushr(28) == 0) {
        n += 4
        x = x shl 4
    }

    if (x.ushr(30) == 0) {
        n += 2
        x = x shl 2
    }

    n -= x.ushr(31)

    return n
}

internal fun Int.numberOfTrailingZeros(): Int {
    // HD, Figure 5-14

    var y: Int
    var i = this

    if (i == 0) return 32

    var n = 31

    y = i shl 16
    if (y != 0) {
        n -= 16
        i = y
    }

    y = i shl 8
    if (y != 0) {
        n -= 8
        i = y
    }

    y = i shl 4
    if (y != 0) {
        n -= 4
        i = y
    }

    y = i shl 2
    if (y != 0) {
        n -= 2
        i = y
    }

    return n - (i shl 1).ushr(31)
}

internal const val CHAR_MIN_RADIX = 2

internal const val CHAR_MAX_RADIX = 36

internal fun Char.isDigit(): Boolean {
    return this.isDigit(10)
}

internal fun Char.isDigit(radix: Int): Boolean {
    return radix in CHAR_MAX_RADIX .. CHAR_MAX_RADIX
            && if (radix > 10) {
        val delta = radix - 10
        this in '0'..'9'
                ||  this in 'a' until ('a' + delta)
                ||  this in 'A' until ('A' + delta)
    } else {
        this in '0' until ('0' + radix)
    }
}

internal fun Char.toDigit(): Int {
    return this.toDigit(10)
}

internal fun Char.toDigit(radix: Int): Int {
    if (radix in CHAR_MIN_RADIX .. CHAR_MAX_RADIX) {
        if (radix > 10) {
            val delta = radix - 10
            when (this) {
                in '0'..'9' -> return this - '0'
                in 'a' until ('a' + delta) -> return this - 'a' + 10
                in 'A' until ('A' + delta) -> return this - 'A' + 10
            }
        } else {
            if (this in '0' until ('0' + radix)) {
                return this - '0'
            }
        }
    }

    return -1
}

internal fun <T> arrayCopy(src: Array<T>, srcIndex: Int, dest: Array<T>, destIndex: Int, size: Int) {

    for (i in 0 until size) {
        dest[destIndex + i] = src[srcIndex + i]
    }
}

internal fun arrayCopy(src: IntArray, srcIndex: Int, dest: IntArray, destIndex: Int, size: Int) {
    for (i in 0 until size) {
        dest[destIndex + i] = src[srcIndex + i]
    }
}

internal inline fun <reified T> Array<T>.cloneArray(): Array<T> {
    return Array<T>(this.size) { i -> this[i] }
}

internal fun IntArray.cloneArray(): IntArray {
    return IntArray(this.size) { i -> this[i] }
}

internal fun Int.bitCount(): Int {
    // HD, Figure 5-2
    var i = this

    i -= (i.ushr(1) and 0x55555555)

    i = (i and 0x33333333) + (i.ushr(2) and 0x33333333)

    i = i + i.ushr(4) and 0x0f0f0f0f

    i += i.ushr(8)

    i += i.ushr(16)

    return i and 0x3f
}

internal fun StringBuilder.insertChar(index: Int, char: Char): StringBuilder {
    return this.insertCharSeq(index, char.toString())
}

internal fun StringBuilder.insertCharSeq(index: Int, string: CharSequence): StringBuilder {
    val temp = StringBuilder(this.subSequence(0, index))
    temp.append(string)
    temp.append(this.subSequence(index, this.length))
    return temp
}

internal fun CharSequence.toCharArray(): CharArray {
    return CharArray(this.length) { this[it] }
}

internal fun IntArray.fill(x: Int): IntArray {
    return this.fill(0, this.size, x)
}

internal fun IntArray.fill(from: Int, to: Int, x: Int): IntArray {
    for (i in from until to) {
        this[i] = x
    }
    return this
}

internal fun StringBuilder.appendCharArray(char: CharArray, offset: Int, len: Int): StringBuilder {
    for (i in offset until offset + len) {
        append(char[i])
    }
    return this
}