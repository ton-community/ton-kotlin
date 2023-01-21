package org.ton.lite.api.liteserver

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.tl.TlConstructor
import org.ton.tl.TlReader
import org.ton.tl.TlWriter

@Serializable
@SerialName("liteServer.sendMsgStatus")
public data class LiteServerSendMsgStatus(
    val status: Int
) {
    public companion object : TlConstructor<LiteServerSendMsgStatus>(
        schema = "liteServer.sendMsgStatus status:int = liteServer.SendMsgStatus"
    ) {
        override fun decode(reader: TlReader): LiteServerSendMsgStatus {
            val status = reader.readInt()
            return LiteServerSendMsgStatus(status)
        }

        override fun encode(writer: TlWriter, value: LiteServerSendMsgStatus) {
            writer.writeInt(value.status)
        }
    }
}
