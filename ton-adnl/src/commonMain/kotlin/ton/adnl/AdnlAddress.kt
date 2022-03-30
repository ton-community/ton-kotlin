package ton.adnl

/**
 * Wrapper struct to hold ADNL address, which is a hash of public key
 */
interface AdnlAddress {
    val bytes: ByteArray
}

expect fun AdnlAddress(bytes: ByteArray): AdnlAddress