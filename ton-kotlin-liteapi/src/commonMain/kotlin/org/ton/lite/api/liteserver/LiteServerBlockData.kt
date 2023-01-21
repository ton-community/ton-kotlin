package org.ton.lite.api.liteserver

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.tl.*

@Serializable
@SerialName("liteServer.blockData")
public class LiteServerBlockData(
    public val id: TonNodeBlockIdExt,
    public val data: ByteArray
) {
    public companion object : TlConstructor<LiteServerBlockData>(
        schema = "liteServer.blockData id:tonNode.blockIdExt data:bytes = liteServer.BlockData"
    ) {
        override fun decode(reader: TlReader): LiteServerBlockData {
            val id = reader.read(TonNodeBlockIdExt)
            val data = reader.readBytes()
            return LiteServerBlockData(id, data)
        }

        override fun encode(writer: TlWriter, value: LiteServerBlockData) {
            writer.write(TonNodeBlockIdExt, value.id)
            writer.writeBytes(value.data)
        }
    }
}
