package org.ton.lite.api.liteserver.functions

import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockId
import org.ton.bitstring.BitString
import org.ton.lite.api.liteserver.LiteServerBlockHeader
import org.ton.tl.*
import kotlin.jvm.JvmStatic

@Serializable
public data class LiteServerLookupBlock(
    val mode: Int,
    val id: TonNodeBlockId,
    val lt: Long?,
    val utime: Int?
) : TLFunction<LiteServerLookupBlock, LiteServerBlockHeader> {
    public constructor(id: TonNodeBlockId, lt: Long?, utime: Int?) : this(
        mode(
            lt != null,
            utime != null
        ), id, lt, utime
    )

    public constructor(id: TonNodeBlockId, lt: Long) : this(id, lt, null)
    public constructor(id: TonNodeBlockId, utime: Int) : this(id, null, utime)

    override fun tlCodec(): TlCodec<LiteServerLookupBlock> = LiteServerLookupBlockTlConstructor
    override fun resultTlCodec(): TlCodec<LiteServerBlockHeader> = LiteServerBlockHeader

    public companion object : TlCodec<LiteServerLookupBlock> by LiteServerLookupBlockTlConstructor {
        @JvmStatic
        public fun mode(
            hasLt: Boolean,
            hasUtime: Boolean
        ): Int {
            var mode = 0
            if (hasLt) mode = mode or 1
            if (hasUtime) mode = mode or 2
            return mode
        }

        @JvmStatic
        public fun byTime(
            id: TonNodeBlockId,
            utime: Int
        ): LiteServerLookupBlock = LiteServerLookupBlock(id, utime)

        @JvmStatic
        public fun byLt(
            id: TonNodeBlockId,
            lt: Long
        ): LiteServerLookupBlock = LiteServerLookupBlock(id, lt)
    }
}

private object LiteServerLookupBlockTlConstructor : TlConstructor<LiteServerLookupBlock>(
    schema = "liteServer.lookupBlock mode:# id:tonNode.blockId lt:mode.1?long utime:mode.2?int = liteServer.BlockHeader"
) {
    override fun decode(reader: TlReader): LiteServerLookupBlock {
        val mode = reader.readInt()
        val id = reader.read(TonNodeBlockId)
        val lt = reader.readNullable(mode, 1, TlReader::readLong)
        val utime = reader.readNullable(mode, 2, TlReader::readInt)
        return LiteServerLookupBlock(mode, id, lt, utime)
    }

    override fun encode(writer: TlWriter, value: LiteServerLookupBlock) {
        val mode = value.mode
        writer.writeInt(mode)
        writer.write(TonNodeBlockId, value.id)
        writer.writeNullable(mode, 1, value.lt, TlWriter::writeLong)
        writer.writeNullable(mode, 2, value.utime, TlWriter::writeInt)
    }
}
