package org.ton.crypto.pbkdf2

expect fun pbkdf2Sha512(key: ByteArray, salt: ByteArray, iterations: Int): ByteArray
