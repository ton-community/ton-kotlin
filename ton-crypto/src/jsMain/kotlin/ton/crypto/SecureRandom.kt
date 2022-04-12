package ton.crypto

import kotlin.random.Random

actual object SecureRandom : Random() {
    override fun nextBits(bitCount: Int): Int {
        TODO("Not yet implemented")
    }
}