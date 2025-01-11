package org.ton.crypto

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.convert
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.usePinned
import platform.windows.BCRYPT_USE_SYSTEM_PREFERRED_RNG
import platform.windows.BCryptGenRandom

@OptIn(ExperimentalForeignApi::class)
public actual fun secureRandom(bytes: ByteArray, fromIndex: Int, toIndex: Int) {
    bytes.usePinned {
        val result = BCryptGenRandom(
            null,
            it.addressOf(fromIndex).reinterpret(),
            (toIndex - fromIndex).convert(),
            BCRYPT_USE_SYSTEM_PREFERRED_RNG.convert()
        )
        if (result != 0) {
            error("Can't generate random values using BCryptGenRandom: $result")
        }
    }
}