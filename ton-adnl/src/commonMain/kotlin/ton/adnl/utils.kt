package ton.adnl

import io.ktor.utils.io.core.*

fun ByteArray.toInt() = buildPacket {
    writeFully(this@toInt, length = 4)
}.readInt()

fun Int.toByteArray() = buildPacket {
    writeInt(this@toByteArray)
}.readBytes(4)