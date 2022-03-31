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
    suspend fun build() = buildPacket(256) {
        writeFully(receiver.bytes)
        writeFully(sender.bytes)

        val rawAesParams = aesParams.build().readBytes()
        val hash = sha256(rawAesParams)

        val key = ByteArray(32)
        sharedKey.bytes.copyInto(key)
        hash.copyInto(key, destinationOffset = 16, startIndex = 16, endIndex = 32)
        val nonce = ByteArray(16)
        hash.copyInto(nonce, endIndex = 4)
        sharedKey.bytes.copyInto(nonce, destinationOffset = 4, startIndex = 20, endIndex = 32)

        val aes = AdnlAes(key, nonce)
        val encryptedAesParams = aes.encrypt(rawAesParams)

        writeFully(hash)
        writeFully(encryptedAesParams)
    }
}