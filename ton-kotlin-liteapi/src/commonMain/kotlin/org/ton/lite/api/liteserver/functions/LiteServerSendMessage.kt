package org.ton.lite.api.liteserver.functions

import kotlinx.serialization.Serializable
import org.ton.block.Message
import org.ton.boc.BagOfCells
import org.ton.cell.Cell
import org.ton.lite.api.liteserver.internal.readBoc
import org.ton.lite.api.liteserver.internal.writeBoc
import org.ton.tl.TlConstructor
import org.ton.tl.TlReader
import org.ton.tl.TlWriter
import org.ton.tlb.CellRef
import org.ton.tlb.constructor.AnyTlbConstructor

@Serializable
public data class LiteServerSendMessage(
    val body: BagOfCells
) {
    public constructor(body: CellRef<Message<Cell>>) : this(BagOfCells(body.toCell(Message.tlbCodec(AnyTlbConstructor))))
    public constructor(body: Message<Cell>) : this(CellRef(body))

    public fun parseBody(): CellRef<Message<Cell>> = CellRef(body.first(), Message.tlbCodec(AnyTlbConstructor))

    public companion object : TlConstructor<LiteServerSendMessage>(
        schema = "liteServer.sendMessage body:bytes = liteServer.SendMsgStatus"
    ) {
        override fun decode(reader: TlReader): LiteServerSendMessage {
            val body = reader.readBoc()
            return LiteServerSendMessage(body)
        }

        override fun encode(writer: TlWriter, value: LiteServerSendMessage) {
            writer.writeBoc(value.body)
        }
    }
}
