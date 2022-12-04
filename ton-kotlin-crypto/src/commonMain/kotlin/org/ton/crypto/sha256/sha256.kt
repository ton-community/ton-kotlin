package org.ton.crypto.sha256

fun sha256(vararg bytes: ByteArray): ByteArray {
    val sha256 = Sha256()
    bytes.forEach {
        sha256.update(it)
    }
    return sha256.digest()
}

expect class Sha256 constructor() {
    fun update(bytes: ByteArray): Sha256
    fun digest(output: ByteArray): ByteArray
}

fun Sha256.digest(): ByteArray = digest(ByteArray(32))
