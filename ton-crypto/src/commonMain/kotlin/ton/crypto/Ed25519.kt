package ton.crypto

import kotlin.random.Random

expect object Ed25519 {
    fun privateKey(random: Random): ByteArray
    fun publicKey(privateKey: ByteArray): ByteArray
    fun sign(privateKey: ByteArray, message: ByteArray): ByteArray
    fun verify(signature: ByteArray, publicKey: ByteArray, message: ByteArray): Boolean
    fun convertToX25519(publicKey: ByteArray): ByteArray
}