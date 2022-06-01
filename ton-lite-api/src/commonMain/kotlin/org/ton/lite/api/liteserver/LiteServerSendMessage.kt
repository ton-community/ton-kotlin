package org.ton.lite.api.liteserver

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.block.Message
import org.ton.block.tlb.tlbCodec
import org.ton.boc.BagOfCells
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.crypto.Base64ByteArraySerializer
import org.ton.crypto.base64
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readBytesTl
import org.ton.tl.constructors.writeBytesTl
import org.ton.tlb.constructor.AnyTlbConstructor
import org.ton.tlb.storeTlb

@Serializable
data class LiteServerSendMessage(
    @Serializable(Base64ByteArraySerializer::class)
    val body: ByteArray
) {
    constructor(bagOfCells: BagOfCells) : this(bagOfCells.toByteArray())
    constructor(message: Message<Cell>) : this(BagOfCells(CellBuilder.createCell {
        storeTlb(Message.tlbCodec(AnyTlbConstructor), message)
    }))

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LiteServerSendMessage

        if (!body.contentEquals(other.body)) return false

        return true
    }

    override fun hashCode(): Int {
        return body.contentHashCode()
    }

    override fun toString(): String = buildString {
        append("LiteServerSendMessage(body=")
        append(base64(body))
        append(")")
    }

    companion object : TlConstructor<LiteServerSendMessage>(
        type = LiteServerSendMessage::class,
        schema = "liteServer.sendMessage body:bytes = liteServer.SendMsgStatus"
    ) {
        override fun decode(input: Input): LiteServerSendMessage {
            val body = input.readBytesTl()
            return LiteServerSendMessage(body)
        }

        override fun encode(output: Output, value: LiteServerSendMessage) {
            output.writeBytesTl(value.body)
        }
    }
}
