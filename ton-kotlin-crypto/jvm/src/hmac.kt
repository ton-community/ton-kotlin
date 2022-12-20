@file:JvmName("HmacJvmKt")

package org.ton.crypto

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

public actual fun hmacSha512(key: ByteArray, input: ByteArray): ByteArray {
    val algorithm = "HmacSHA512"

    val ctx = Mac.getInstance(algorithm)
    ctx.init(SecretKeySpec(key, algorithm))
    ctx.update(input)
    return ctx.doFinal()
}
