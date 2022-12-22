package org.ton.lite.api.liteserver.functions

import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.lite.api.liteserver.LiteServerValidatorStats
import org.ton.tl.*


@Serializable
public data class LiteServerGetValidatorStats(
    val mode: Int,
    val id: TonNodeBlockIdExt,
    val limit: Int,
    val startAfter: Bits256?,
    val modifiedAfter: Int?
) : TLFunction<LiteServerGetValidatorStats, LiteServerValidatorStats> {
    public constructor(
        mode: Int,
        id: TonNodeBlockIdExt,
        limit: Int,
        startAfter: ByteArray? = null,
        modifiedAfter: Int? = null
    ) : this(mode, id, limit, startAfter?.let { Bits256(it) }, modifiedAfter)

    public companion object : TlCodec<LiteServerGetValidatorStats> by LiteServerGetValidatorStatsTlConstructor

    override fun tlCodec(): TlCodec<LiteServerGetValidatorStats> = LiteServerGetValidatorStats

    override fun resultTlCodec(): TlCodec<LiteServerValidatorStats> = LiteServerValidatorStats
}

private object LiteServerGetValidatorStatsTlConstructor : TlConstructor<LiteServerGetValidatorStats>(
    schema = "liteServer.getValidatorStats#091a58bc mode:# id:tonNode.blockIdExt limit:int start_after:mode.0?int256 modified_after:mode.2?int = liteServer.ValidatorStats",
    id = 0x091a58bc
) {
    override fun decode(reader: TlReader): LiteServerGetValidatorStats {
        val mode = reader.readInt()
        val id = reader.read(TonNodeBlockIdExt)
        val limit = reader.readInt()
        val startAfter = reader.readNullable(mode, 0, TlReader::readBits256)
        val modifiedAfter = reader.readNullable(mode, 2, TlReader::readInt)
        return LiteServerGetValidatorStats(mode, id, limit, startAfter, modifiedAfter)
    }

    override fun encode(writer: TlWriter, value: LiteServerGetValidatorStats) {
        writer.writeInt(value.mode)
        writer.write(TonNodeBlockIdExt, value.id)
        writer.writeInt(value.limit)
        writer.writeNullable(value.mode, 0, value.startAfter, TlWriter::writeBits256)
        writer.writeNullable(value.mode, 2, value.modifiedAfter, TlWriter::writeInt)
    }
}
