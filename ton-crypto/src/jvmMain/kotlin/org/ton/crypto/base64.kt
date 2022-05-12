package org.ton.crypto

import java.util.*

actual fun base64(string: String): ByteArray = Base64.getDecoder().decode(string)

actual fun base64(byteArray: ByteArray): String = Base64.getEncoder().encodeToString(byteArray)