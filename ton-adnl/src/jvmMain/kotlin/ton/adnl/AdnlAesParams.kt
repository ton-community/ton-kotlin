package ton.adnl

import java.security.SecureRandom

/**
 * Session parameters for AES-CTR encryption of datagrams
 */
data class AdnlAesParams(
    val rxKey: ByteArray = ByteArray(32),
    val txKey: ByteArray = ByteArray(32),
    val rxNonce: ByteArray = ByteArray(16),
    val txNonce: ByteArray = ByteArray(16),
    val padding: ByteArray = ByteArray(64),
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AdnlAesParams

        if (!rxKey.contentEquals(other.rxKey)) return false
        if (!txKey.contentEquals(other.txKey)) return false
        if (!rxNonce.contentEquals(other.rxNonce)) return false
        if (!txNonce.contentEquals(other.txNonce)) return false
        if (!padding.contentEquals(other.padding)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = rxKey.contentHashCode()
        result = 31 * result + txKey.contentHashCode()
        result = 31 * result + rxNonce.contentHashCode()
        result = 31 * result + txNonce.contentHashCode()
        result = 31 * result + padding.contentHashCode()
        return result
    }

    companion object {
        fun random() = AdnlAesParams().apply {
            val secureRandom = SecureRandom.getInstanceStrong()
            secureRandom.nextBytes(rxKey)
            secureRandom.nextBytes(txKey)
            secureRandom.nextBytes(rxNonce)
            secureRandom.nextBytes(txNonce)
            secureRandom.nextBytes(padding)
        }
    }
}
