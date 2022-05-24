package org.ton.crypto

expect fun hmacSha512(key: ByteArray, input: ByteArray): ByteArray

fun hmacSha512(phrase: String, password: String = "") = hmacSha512(phrase.toByteArray(), password.toByteArray())
