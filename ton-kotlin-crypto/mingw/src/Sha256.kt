package org.ton.crypto

import org.ton.crypto.internal.Sha256Impl

public actual class Sha256 {
    private val impl = Sha256Impl()

    public actual fun update(bytes: ByteArray): Sha256 = apply {
        impl.update(bytes)
    }

    public actual fun digest(output: ByteArray): ByteArray {
        return impl.digest(output)
    }
}
