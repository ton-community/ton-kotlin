package org.ton.crypto

import org.ton.crypto.base64.Base64
import org.ton.crypto.base64.Base64Url

fun base64(string: String): ByteArray = Base64.decode(string)

fun base64(byteArray: ByteArray): String = Base64.encode(byteArray)

fun base64url(string: String): ByteArray = Base64Url.decode(string)

fun base64url(byteArray: ByteArray): String = Base64Url.encode(byteArray)
