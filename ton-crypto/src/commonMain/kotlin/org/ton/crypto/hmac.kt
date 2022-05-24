package org.ton.crypto

expect fun hmacSha512(key: ByteArray, input: ByteArray): ByteArray
