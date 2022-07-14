package org.ton.adnl.node

import org.ton.crypto.sha256

class AdnlSubChannelSide(
    val id: ByteArray = ByteArray(32),
    val secret: ByteArray = ByteArray(32),
    val priority: Boolean = false
) {
    fun encrypt(payload: ByteArray): ByteArray {
        val cipher = AdnlAesCipher.secure(secret, payload)
        val result = ByteArray(64 + payload.size)

        val checksum = sha256(payload)
        val data = cipher.encrypt(payload)

        checksum.copyInto(result, 0)
        id.copyInto(result, 32)
        data.copyInto(result, 64)

        return result
    }
}

class AdnlChannelSide(
    val ordinary: AdnlSubChannelSide = AdnlSubChannelSide(),
    val priority: AdnlSubChannelSide = AdnlSubChannelSide()
)

class AdnlChannel(
    val recv: AdnlChannelSide = AdnlChannelSide(),
    val send: AdnlChannelSide = AdnlChannelSide()
) {
    companion object {
        fun zero() = AdnlChannel()
    }
}
