package org.ton.lite.api.liteserver

import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.block.Block
import org.ton.boc.BagOfCells
import org.ton.lite.api.liteserver.internal.readBoc
import org.ton.lite.api.liteserver.internal.writeBoc
import org.ton.tl.*
import org.ton.tlb.CellRef

@Serializable
public data class LiteServerBlockData(
    val id: TonNodeBlockIdExt,
    val data: BagOfCells
) {
    public fun dataAsBlock(): CellRef<Block> = CellRef(data.first(), Block)

    public companion object : TlConstructor<LiteServerBlockData>(
        schema = "liteServer.blockData id:tonNode.blockIdExt data:bytes = liteServer.BlockData"
    ) {
        override fun decode(reader: TlReader): LiteServerBlockData {
            val id = reader.read(TonNodeBlockIdExt)
            val data = reader.readBoc()
            return LiteServerBlockData(id, data)
        }

        override fun encode(writer: TlWriter, value: LiteServerBlockData) {
            writer.write(TonNodeBlockIdExt, value.id)
            writer.writeBoc(value.data)
        }
    }
}
