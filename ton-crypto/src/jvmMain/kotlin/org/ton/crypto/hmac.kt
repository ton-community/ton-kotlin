package org.ton.crypto

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

actual fun hmac_sha512(key: ByteArray, input: ByteArray): ByteArray {
    val algorithm = "HmacSHA512"

    val ctx = Mac.getInstance(algorithm)
    ctx.init( SecretKeySpec(key,algorithm ))
    ctx.update(input)
    return ctx.doFinal()
}
