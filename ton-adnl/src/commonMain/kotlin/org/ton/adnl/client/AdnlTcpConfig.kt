package org.ton.adnl.client

import org.ton.api.pub.PublicKey
import org.ton.crypto.SecureRandom
import kotlin.random.Random

data class AdnlTcpConfig(
    val serverPublicKey: PublicKey,
    val random: Random = SecureRandom
)

data class AdnlTcpConfigBuilder(
    var random: Random? = null,
    var serverPublicKey: PublicKey? = null
) {
    fun build(): AdnlTcpConfig = AdnlTcpConfig(
        requireNotNull(serverPublicKey),
        random ?: SecureRandom
    )
}
