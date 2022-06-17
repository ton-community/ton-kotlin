package org.ton.lite.api.liteserver

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readIntTl
import org.ton.tl.constructors.writeIntTl

@Serializable
data class LiteServerSendMsgStatus(
    val status: Int
) {
    companion object : TlConstructor<LiteServerSendMsgStatus>(
        type = LiteServerSendMsgStatus::class,
        schema = "liteServer.sendMsgStatus status:int = liteServer.SendMsgStatus"
    ) {
        override fun decode(input: Input): LiteServerSendMsgStatus {
            val status = input.readIntTl()
            return LiteServerSendMsgStatus(status)
        }

        override fun encode(output: Output, value: LiteServerSendMsgStatus) {
            output.writeIntTl(value.status)
        }
    }
}
