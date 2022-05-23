package org.ton.crypto

expect fun hmac_sha512(key: ByteArray, input: ByteArray): ByteArray

fun hmac_sha512(phrase: String, password: String = "") = hmac_sha512(phrase.toByteArray(), password.toByteArray())
