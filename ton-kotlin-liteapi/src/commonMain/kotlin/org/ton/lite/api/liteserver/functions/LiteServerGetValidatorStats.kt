package org.ton.lite.api.liteserver.functions

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.lite.api.liteserver.LiteServerValidatorStats
import org.ton.tl.*

@Serializable
@SerialName("liteServer.getValidatorStats")
public data class LiteServerGetValidatorStats(
    val mode: Int,
    val id: TonNodeBlockIdExt,
    val limit: Int,
    val startAfter: ByteArray?,
    val modifiedAfter: Int?
) : TLFunction<LiteServerGetValidatorStats, LiteServerValidatorStats> {
    init {
        require(startAfter == null || startAfter.size == 32) { "startAfter must be 32 bytes long" }
    }

    public companion object : TlCodec<LiteServerGetValidatorStats> by LiteServerGetValidatorStatsTlConstructor

    override fun tlCodec(): TlCodec<LiteServerGetValidatorStats> = LiteServerGetValidatorStats

    override fun resultTlCodec(): TlCodec<LiteServerValidatorStats> = LiteServerValidatorStats

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LiteServerGetValidatorStats) return false

        if (mode != other.mode) return false
        if (id != other.id) return false
        if (limit != other.limit) return false
        if (startAfter != null) {
            if (other.startAfter == null) return false
            if (!startAfter.contentEquals(other.startAfter)) return false
        } else if (other.startAfter != null) return false
        if (modifiedAfter != other.modifiedAfter) return false

        return true
    }

    override fun hashCode(): Int {
        var result = mode
        result = 31 * result + id.hashCode()
        result = 31 * result + limit
        result = 31 * result + (startAfter?.contentHashCode() ?: 0)
        result = 31 * result + (modifiedAfter ?: 0)
        return result
    }
}

private object LiteServerGetValidatorStatsTlConstructor : TlConstructor<LiteServerGetValidatorStats>(
    schema = "liteServer.getValidatorStats#091a58bc mode:# id:tonNode.blockIdExt limit:int start_after:mode.0?int256 modified_after:mode.2?int = liteServer.ValidatorStats",
    id = 0x091a58bc
) {
    override fun decode(reader: TlReader): LiteServerGetValidatorStats {
        val mode = reader.readInt()
        val id = reader.read(TonNodeBlockIdExt)
        val limit = reader.readInt()
        val startAfter = reader.readNullable(mode, 0) {
            readRaw(32)
        }
        val modifiedAfter = reader.readNullable(mode, 2, TlReader::readInt)
        return LiteServerGetValidatorStats(mode, id, limit, startAfter, modifiedAfter)
    }

    override fun encode(writer: TlWriter, value: LiteServerGetValidatorStats) {
        writer.writeInt(value.mode)
        writer.write(TonNodeBlockIdExt, value.id)
        writer.writeInt(value.limit)
        writer.writeNullable(value.mode, 0, value.startAfter) {
            writeRaw(it)
        }
        writer.writeNullable(value.mode, 2, value.modifiedAfter, TlWriter::writeInt)
    }
}
