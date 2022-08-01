package org.ton.adnl.client

import org.ton.api.pub.PublicKey
import org.ton.crypto.SecureRandom
import kotlin.random.Random

data class AdnlConfig(
    val serverPublicKey: PublicKey,
    val random: Random = SecureRandom
)

data class AdnlConfigBuilder(
    var random: Random? = null,
    var serverPublicKey: PublicKey? = null
) {
    fun build(): AdnlConfig = AdnlConfig(
        requireNotNull(serverPublicKey),
        random ?: SecureRandom
    )
}
