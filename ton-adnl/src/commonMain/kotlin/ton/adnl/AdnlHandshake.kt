package ton.adnl

import io.ktor.utils.io.core.*
import ton.crypto.sha256

/**
 * Handshake packet, must be sent from client to server prior to any datagrams
 */
data class AdnlHandshake(
    val receiver: AdnlAddress,
    val sender: AdnlPublicKey,
    val aesParams: AdnlAesParams,
    val sharedKey: AdnlSharedKey,
) {
    fun build() = buildPacket {
        writeFully(receiver.value)
        writeFully(sender.value)

        val rawAesParams = aesParams.build().readBytes()
        val hash = sha256(rawAesParams)

        val key = ByteArray(32).apply {
            sharedKey.value.copyInto(this)
            hash.copyInto(this, destinationOffset = 16, startIndex = 16, endIndex = 32)
        }
        val nonce = ByteArray(16).apply {
            hash.copyInto(this, endIndex = 4)
            sharedKey.value.copyInto(this, destinationOffset = 4, startIndex = 20, endIndex = 32)
        }

        val aes = AdnlAes(key, nonce)
        val encryptedAesParams = aes.encrypt(rawAesParams)

        writeFully(hash)
        writeFully(encryptedAesParams)
    }
}