package org.ton.crypto.base64

import org.ton.crypto.base64.internal.BASE64
import org.ton.crypto.base64.internal.commonDecodeBase64ToArray
import org.ton.crypto.base64.internal.commonEncodeBse64

object Base64 {
    fun encode(value: ByteArray): String = value.commonEncodeBse64(BASE64)

    fun decode(value: String): ByteArray =
        value.commonDecodeBase64ToArray()
            ?: throw IllegalArgumentException("Can't decode string: '${value.replace("\n", "")}'")
}
