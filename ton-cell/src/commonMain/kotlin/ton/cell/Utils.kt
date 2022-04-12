package ton.cell

import io.ktor.utils.io.core.*

internal fun Input.readInt(bytes: Int): Int {
    var res = 0
    var b = bytes
    while (b > 0) {
        res = (res shl 8) + readByte()
        b--
    }
    return res
}