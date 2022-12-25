package org.ton.crypto

public expect fun pbkdf2Sha512(key: ByteArray, salt: ByteArray, iterations: Int): ByteArray
