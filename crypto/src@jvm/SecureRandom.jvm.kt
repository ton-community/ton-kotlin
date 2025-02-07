package org.ton.crypto

public actual fun secureRandom(array: ByteArray, fromIndex: Int, toIndex: Int) {
    val javaSecureRandom = java.security.SecureRandom()
    val tmp = ByteArray(toIndex - fromIndex)
    javaSecureRandom.nextBytes(tmp)
    tmp.copyInto(array, fromIndex)
}