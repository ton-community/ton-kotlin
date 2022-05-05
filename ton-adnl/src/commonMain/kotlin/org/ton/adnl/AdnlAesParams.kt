package org.ton.adnl

import io.ktor.utils.io.core.*
import org.ton.crypto.SecureRandom
import kotlin.random.Random

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
        if (other == null || this::class != other::class) return false

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

    fun build() = buildPacket(160) {
        writeFully(rxKey)
        writeFully(txKey)
        writeFully(rxNonce)
        writeFully(txNonce)
        writeFully(padding)
    }

    companion object {
        fun random(random: Random = SecureRandom): AdnlAesParams {
            val adnlAesParams = AdnlAesParams()
            random.nextBytes(adnlAesParams.rxKey)
            random.nextBytes(adnlAesParams.txKey)
            random.nextBytes(adnlAesParams.rxNonce)
            random.nextBytes(adnlAesParams.txNonce)
            random.nextBytes(adnlAesParams.padding)
            return adnlAesParams
        }
    }
}
