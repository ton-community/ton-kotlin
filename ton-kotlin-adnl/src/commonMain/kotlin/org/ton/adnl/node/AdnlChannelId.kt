package org.ton.adnl.node

import org.ton.api.pk.PrivateKeyAes
import org.ton.api.pub.PublicKeyAes

data class AdnlChannelId(
    val value: ByteArray
) {
    constructor(publicKey: PublicKeyAes) : this(PublicKeyAes.hash(publicKey))
    constructor(privateKey: PrivateKeyAes) : this(PrivateKeyAes.hash(privateKey))

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AdnlChannelId) return false
        if (!value.contentEquals(other.value)) return false
        return true
    }

    override fun hashCode(): Int = value.contentHashCode()
}