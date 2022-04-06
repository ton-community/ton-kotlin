package ton.adnl

/**
 * Wrapper struct to hold ADNL address, which is a hash of public key
 */
@JvmInline
value class AdnlAddress(
    val value: ByteArray,
)