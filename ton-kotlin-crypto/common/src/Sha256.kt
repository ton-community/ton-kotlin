package org.ton.crypto

public fun Sha256.digest(): ByteArray = digest(ByteArray(32))

public fun sha256(vararg bytes: ByteArray): ByteArray {
    val sha256 = Sha256()
    bytes.forEach {
        sha256.update(it)
    }
    return sha256.digest()
}

public expect class Sha256 constructor() {
    public fun update(bytes: ByteArray): Sha256
    public fun digest(output: ByteArray): ByteArray
}
