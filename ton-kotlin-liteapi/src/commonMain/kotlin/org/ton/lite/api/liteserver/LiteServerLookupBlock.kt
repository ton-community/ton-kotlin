package org.ton.lite.api.liteserver

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockId
import org.ton.lite.api.liteserver.functions.LiteServerQueryFunction
import org.ton.tl.*
import org.ton.tl.constructors.IntTlConstructor
import org.ton.tl.constructors.LongTlConstructor
import org.ton.tl.constructors.readIntTl
import org.ton.tl.constructors.writeIntTl

interface LiteServerLookupBlockFunction : LiteServerQueryFunction {
    suspend fun query(query: LiteServerLookupBlock) =
        query(query, LiteServerLookupBlock, LiteServerBlockHeader)

    suspend fun lookupBlock(
        id: TonNodeBlockId,
        lt: Long? = null,
        utime: Int? = null
    ): LiteServerBlockHeader {
        val query = if (utime != null) LiteServerLookupBlock(id, utime)
        else if (lt != null) LiteServerLookupBlock(id, lt)
        else LiteServerLookupBlock(id)
        return query(query)
    }

    suspend fun lookupBlockByUtime(
        id: TonNodeBlockId,
        utime: Int
    ) = query(LiteServerLookupBlock(id, utime))

    suspend fun lookupBlockByLt(
        id: TonNodeBlockId,
        lt: Long
    ) = query(LiteServerLookupBlock(id, lt))
}

@Serializable
data class LiteServerLookupBlock(
    val mode: Int,
    val id: TonNodeBlockId,
    val lt: Long?,
    val utime: Int?
) {
    constructor(id: TonNodeBlockId) : this(0b001, id, null, null)
    constructor(id: TonNodeBlockId, lt: Long) : this(0b010, id, lt, null)
    constructor(id: TonNodeBlockId, utime: Int) : this(0b100, id, null, utime)

    companion object : TlCodec<LiteServerLookupBlock> by LiteServerLookupBlockTlConstructor
}

private object LiteServerLookupBlockTlConstructor : TlConstructor<LiteServerLookupBlock>(
    schema = "liteServer.lookupBlock mode:# id:tonNode.blockId lt:mode.1?long utime:mode.2?int = liteServer.BlockHeader"
) {
    override fun decode(input: Input): LiteServerLookupBlock {
        val mode = input.readIntTl()
        val id = input.readTl(TonNodeBlockId)
        val lt = input.readFlagTl(mode, 1, LongTlConstructor)
        val utime = input.readFlagTl(mode, 2, IntTlConstructor)
        return LiteServerLookupBlock(mode, id, lt, utime)
    }

    override fun encode(output: Output, value: LiteServerLookupBlock) {
        output.writeIntTl(value.mode)
        output.writeTl(TonNodeBlockId, value.id)
        if (value.lt != null) output.writeOptionalTl(value.mode, 1, LongTlConstructor, value.lt)
        if (value.utime != null) output.writeOptionalTl(value.utime, 2, IntTlConstructor, value.utime)
    }
}
