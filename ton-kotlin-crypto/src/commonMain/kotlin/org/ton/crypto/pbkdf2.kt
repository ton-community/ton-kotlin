package org.ton.crypto

expect fun pbkdf2Sha512(key: ByteArray, salt: ByteArray, iterations: Int): ByteArray
