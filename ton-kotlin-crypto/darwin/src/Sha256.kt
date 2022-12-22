package org.ton.crypto

import kotlinx.cinterop.*
import platform.CoreCrypto.CC_SHA256_CTX
import platform.CoreCrypto.CC_SHA256_Final
import platform.CoreCrypto.CC_SHA256_Init
import platform.CoreCrypto.CC_SHA256_Update
import kotlin.native.internal.createCleaner

public actual class Sha256 {
    private val sha256Ctx = nativeHeap.alloc<CC_SHA256_CTX>().also {
        CC_SHA256_Init(it.ptr)
    }

    @OptIn(ExperimentalStdlibApi::class)
    private val cleaner = createCleaner(sha256Ctx) {
        nativeHeap.free(it)
    }

    public actual fun update(bytes: ByteArray): Sha256 {
        bytes.usePinned { pinned ->
            CC_SHA256_Update(sha256Ctx.ptr, pinned.addressOf(0), bytes.size.convert())
        }
        return this
    }

    public actual fun digest(output: ByteArray): ByteArray = memScoped {
        output.usePinned {
            CC_SHA256_Final(it.addressOf(0).reinterpret(), sha256Ctx.ptr)
        }
        output
    }
}
