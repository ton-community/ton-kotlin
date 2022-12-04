package org.ton.crypto.hmac

expect fun hmacSha512(key: ByteArray, input: ByteArray): ByteArray
