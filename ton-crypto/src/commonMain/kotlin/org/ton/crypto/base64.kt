package org.ton.crypto

expect fun base64(string: String): ByteArray

expect fun base64(byteArray: ByteArray): String

expect fun base64url(string: String): ByteArray

expect fun base64url(byteArray: ByteArray): String