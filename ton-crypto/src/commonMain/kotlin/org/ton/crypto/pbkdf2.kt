package org.ton.crypto

expect fun pbkdf2_sha512(key: CharArray, salt: ByteArray, iterations: Int): ByteArray

fun pbkdf2_sha512(key: ByteArray, salt: String, iterations: Int): ByteArray = pbkdf2_sha512(key, salt.toByteArray(), iterations)
