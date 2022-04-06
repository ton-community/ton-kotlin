package ton.crypto.curve25519

internal interface Constants {
    /**
     * Precomputed value of one of the square roots of -1 (mod p)
     */
    val SQRT_M1: FieldElement
}