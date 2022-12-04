package org.ton.crypto.random

import kotlinx.cinterop.*
import platform.Security.SecRandomCopyBytes
import platform.Security.kSecRandomDefault
import kotlin.random.Random

actual object SecureRandom : Random() {
    override fun nextBits(bitCount: Int): Int = nextInt().takeUpperBits(bitCount)

    override fun nextInt(): Int = memScoped {
        val int = alloc<IntVar>()
        SecRandomCopyBytes(
            kSecRandomDefault,
            32,
            int.ptr
        )
        return int.value
    }

    override fun nextBytes(array: ByteArray, fromIndex: Int, toIndex: Int): ByteArray {
        array.usePinned {
            SecRandomCopyBytes(
                kSecRandomDefault,
                (toIndex - fromIndex).convert(),
                it.addressOf(fromIndex)
            )
        }
        return array
    }
}
