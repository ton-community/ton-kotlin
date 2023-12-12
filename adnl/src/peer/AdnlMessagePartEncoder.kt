package org.ton.adnl.peer

import org.ton.api.adnl.message.AdnlMessage
import org.ton.api.adnl.message.AdnlMessagePart
import org.ton.crypto.digest.sha256
import org.ton.tl.asByteString

public class AdnlMessagePartEncoder(
    public val mtu: Int,
    public val maxSize: Int = 1024 * 8
) {
    public fun encode(message: AdnlMessage): List<AdnlMessagePart> {
        val newMessages = ArrayList<AdnlMessagePart>()
        val b = AdnlMessage.encodeToByteArray(message, boxed = true)
        check(b.size <= maxSize)
        val hash = sha256(b).asByteString()
        val size = b.size
        var offset = 0
        while (offset < size) {
            val data = b.copyOfRange(offset, minOf(offset + mtu, b.size))
            val partMessage = AdnlMessagePart(
                hash,
                size,
                offset,
                data.asByteString()
            )
            newMessages.add(partMessage)
            offset += mtu
        }
        return newMessages
    }
}
