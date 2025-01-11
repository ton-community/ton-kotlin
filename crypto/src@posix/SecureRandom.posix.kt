package org.ton.crypto

import kotlinx.cinterop.*
import platform.posix.fclose
import platform.posix.fopen
import platform.posix.fread

@OptIn(ExperimentalForeignApi::class, UnsafeNumber::class)
public actual fun secureRandom(bytes: ByteArray, fromIndex: Int, toIndex: Int) {
    val fd = fopen("/dev/urandom", "rb") ?: error("Can't open /dev/urandom")
    try {
        bytes.usePinned {
            fread(it.addressOf(fromIndex), 1u, (toIndex - fromIndex).convert(), fd)
        }
    } catch (e: Throwable) {
        fclose(fd)
        throw e
    }
}