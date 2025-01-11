package org.ton.crypto

public actual fun secureRandom(bytes: ByteArray, fromIndex: Int, toIndex: Int) {
    val javaSecureRandom = java.security.SecureRandom()
    val tmp = ByteArray(toIndex - fromIndex)
    javaSecureRandom.nextBytes(tmp)
    tmp.copyInto(bytes, fromIndex)
}