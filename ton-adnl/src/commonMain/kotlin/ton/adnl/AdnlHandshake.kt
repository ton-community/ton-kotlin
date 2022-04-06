package ton.adnl

import io.ktor.utils.io.core.*
import ton.crypto.hex
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
    suspend fun build() = buildPacket {
        writeFully(receiver.bytes)
        writeFully(sender.bytes)

        val rawAesParams = aesParams.build().readBytes()
        val hash = sha256(rawAesParams)

        val key = ByteArray(32).apply {
            sharedKey.bytes.copyInto(this)
            hash.copyInto(this, destinationOffset = 16, startIndex = 16, endIndex = 32)
        }
        val nonce = ByteArray(16).apply {
            hash.copyInto(this, endIndex = 4)
            sharedKey.bytes.copyInto(this, destinationOffset = 4, startIndex = 20, endIndex = 32)
        }

        val aes = AdnlAes(key, nonce)
        val encryptedAesParams = aes.encrypt(rawAesParams)

        writeFully(hash)
        writeFully(encryptedAesParams)

        println("receiver=${hex(receiver.bytes)}")
        println("sender=${hex(receiver.bytes)}")
        println("hash=${hex(hash)}")
        println("unencrypted=${hex(rawAesParams)}")
        println("encrypted=${hex(rawAesParams)}")
    }
}