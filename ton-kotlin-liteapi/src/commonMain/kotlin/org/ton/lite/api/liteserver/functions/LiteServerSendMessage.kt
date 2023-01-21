package org.ton.lite.api.liteserver.functions

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.lite.api.liteserver.LiteServerSendMsgStatus
import org.ton.tl.*

@Serializable
@SerialName("liteServer.sendMessage")
public data class LiteServerSendMessage(
    val body: ByteArray
) : TLFunction<LiteServerSendMessage, LiteServerSendMsgStatus> {
    override fun tlCodec(): TlCodec<LiteServerSendMessage> = LiteServerSendMessageTlConstructor

    override fun resultTlCodec(): TlCodec<LiteServerSendMsgStatus> = LiteServerSendMsgStatus

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LiteServerSendMessage) return false
        if (!body.contentEquals(other.body)) return false
        return true
    }

    override fun hashCode(): Int {
        return body.contentHashCode()
    }

    public companion object : TlCodec<LiteServerSendMessage> by LiteServerSendMessageTlConstructor
}

private object LiteServerSendMessageTlConstructor : TlConstructor<LiteServerSendMessage>(
    schema = "liteServer.sendMessage body:bytes = liteServer.SendMsgStatus"
) {
    override fun decode(reader: TlReader): LiteServerSendMessage {
        val body = reader.readBytes()
        return LiteServerSendMessage(body)
    }

    override fun encode(writer: TlWriter, value: LiteServerSendMessage) {
        writer.writeBytes(value.body)
    }
}
