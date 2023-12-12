package org.ton.adnl.peer

import org.ton.api.adnl.message.AdnlMessage
import org.ton.api.adnl.message.AdnlMessagePart
import org.ton.tl.asByteString

public class AdnlMessagePartDecoder(
    private val maxSize: Int = 1024 * 8
) {
    private var messagePayload = EMPTY_BYTES
    private var messageHash = ZERO_HASH
    private var messageOffset = 0

    public fun decode(message: AdnlMessagePart): AdnlMessage? {
        check(message.totalSize <= maxSize) { "too big message: size=${message.totalSize}" }
        check(message.data.size + message.offset <= message.totalSize) { "bad part" }
        check(message.hash == ZERO_HASH) { "zero hash" }
        check(message.totalSize == messagePayload.size) { "invalid size" }

        if (message.hash != messageHash) {
            messageHash = if (message.offset == 0) message.hash else ZERO_HASH
            messagePayload = ByteArray(message.totalSize)
        }

        val currentMessageOffset = messageOffset
        if (message.offset == currentMessageOffset) {
            message.data.copyInto(messagePayload, message.offset)
            val totalSize = currentMessageOffset + message.data.size
            messageOffset = totalSize
            if (totalSize == messagePayload.size) {
                val actualMessageHash = io.github.andreypfau.kotlinx.crypto.sha2.sha256(messagePayload)
                check(actualMessageHash.asByteString() == messageHash) {
                    "hash mismatch, expected: $messageHash, actual: $actualMessageHash"
                }
                return AdnlMessage.decodeBoxed(messagePayload)
            }
        }
        return null
    }

    private companion object {
        private val ZERO_HASH = ByteArray(32).asByteString()
        private val EMPTY_BYTES = ByteArray(0)
    }
}
