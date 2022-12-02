package org.ton.crypto.base64

import org.ton.crypto.base64.internal.BASE64_URL
import org.ton.crypto.base64.internal.commonDecodeBase64ToArray
import org.ton.crypto.base64.internal.commonEncodeBse64

object Base64Url {
    fun encode(value: ByteArray): String = value.commonEncodeBse64(BASE64_URL)

    fun decode(value: String): ByteArray =
        value.commonDecodeBase64ToArray()
            ?: throw IllegalArgumentException("Can't decode string: '${value.replace("\n", "")}'")
}
