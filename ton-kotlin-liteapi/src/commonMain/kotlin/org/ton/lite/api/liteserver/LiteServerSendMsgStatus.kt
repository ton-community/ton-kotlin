package org.ton.lite.api.liteserver

import kotlinx.serialization.Serializable
import org.ton.tl.TlConstructor
import org.ton.tl.TlReader
import org.ton.tl.TlWriter

@Serializable
public data class LiteServerSendMsgStatus(
    val status: Int
) {
    public companion object : TlConstructor<LiteServerSendMsgStatus>(
        schema = "liteServer.sendMsgStatus status:int = liteServer.SendMsgStatus"
    ) {
        override fun decode(input: TlReader): LiteServerSendMsgStatus {
            val status = input.readInt()
            return LiteServerSendMsgStatus(status)
        }

        override fun encode(output: TlWriter, value: LiteServerSendMsgStatus) {
            output.writeInt(value.status)
        }
    }
}
