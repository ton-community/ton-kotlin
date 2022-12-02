package org.ton.crypto

import kotlinx.cinterop.addressOf
import kotlinx.cinterop.convert
import kotlinx.cinterop.usePinned
import platform.posix.fclose
import platform.posix.fopen
import platform.posix.fread
import kotlin.random.Random

actual object SecureRandom : Random() {
    override fun nextBits(bitCount: Int): Int {
        val fd = fopen("/dev/urandom", "rb") ?: return 0
        val bytes = ByteArray(4)
        val size = bytes.size
        bytes.usePinned { pinned ->
            fread(pinned.addressOf(0), 1.convert(), size.convert(), fd)
        }
        fclose(fd)
        return bytes.getIntAt(0)
    }
}
