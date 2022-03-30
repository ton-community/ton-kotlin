package ton.adnl

/**
 * Trait which must be implemented to perform key agreement inside [`AdnlHandshake`]
 */
interface AdnlPrivateKey {
    val bytes: ByteArray

    /**
     * Get public key corresponding to this private
     */
    suspend fun public(): AdnlPublicKey

    /**
     * Perform key agreement protocol (usually x25519) between our private key
     * and their public
     */
    suspend fun sharedKey(publicKey: AdnlPublicKey): AdnlSharedKey
}

expect fun AdnlPrivateKey(bytes: ByteArray): AdnlPrivateKey

/**
 * Public key can be provided using raw slice
 */
interface AdnlPublicKey {
    val bytes: ByteArray

    suspend fun address(): AdnlAddress
}

expect fun AdnlPublicKey(bytes: ByteArray): AdnlPublicKey

/**
 * Wrapper struct to hold the secret, result of ECDH between peers
 */
interface AdnlSharedKey {
    val bytes: ByteArray
}