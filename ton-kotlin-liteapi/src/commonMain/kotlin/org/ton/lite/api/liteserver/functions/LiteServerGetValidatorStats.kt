package org.ton.lite.api.liteserver.functions

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.crypto.encodeHex
import org.ton.lite.api.liteserver.LiteServerValidatorStats
import org.ton.tl.*
import org.ton.tl.constructors.Int256TlConstructor
import org.ton.tl.constructors.IntTlConstructor
import org.ton.tl.constructors.readIntTl
import org.ton.tl.constructors.writeIntTl

interface LiteServerGetValidatorStatsFunction : LiteServerQueryFunction {
    suspend fun query(query: LiteServerGetValidatorStats) =
        query(query, LiteServerGetValidatorStats, LiteServerValidatorStats)

    suspend fun getValidatorStats(
        mode: Int,
        id: TonNodeBlockIdExt,
        limit: Int,
        start_after: ByteArray?,
        modified_after: Int?
    ) = query(LiteServerGetValidatorStats(mode, id, limit, start_after, modified_after))
}

@Serializable
data class LiteServerGetValidatorStats(
    val mode: Int,
    val id: TonNodeBlockIdExt,
    val limit: Int,
    val start_after: ByteArray?,
    val modified_after: Int?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LiteServerGetValidatorStats) return false
        if (mode != other.mode) return false
        if (id != other.id) return false
        if (limit != other.limit) return false
        if (start_after != null) {
            if (other.start_after == null) return false
            if (!start_after.contentEquals(other.start_after)) return false
        } else if (other.start_after != null) return false
        if (modified_after != other.modified_after) return false
        return true
    }

    override fun hashCode(): Int {
        var result = mode
        result = 31 * result + id.hashCode()
        result = 31 * result + limit
        result = 31 * result + (start_after?.contentHashCode() ?: 0)
        result = 31 * result + (modified_after ?: 0)
        return result
    }

    override fun toString(): String = buildString {
        append("LiteServerGetValidatorStats(mode=")
        append(mode)
        append(", id=")
        append(id)
        append(", limit=")
        append(limit)
        append(", start_after=")
        append(start_after?.encodeHex())
        append(", modified_after=")
        append(modified_after)
        append(")")
    }

    companion object : TlCodec<LiteServerGetValidatorStats> by LiteServerGetValidatorStatsTlConstructor
}

private object LiteServerGetValidatorStatsTlConstructor : TlConstructor<LiteServerGetValidatorStats>(
    schema = "liteServer.getValidatorStats#091a58bc mode:# id:tonNode.blockIdExt limit:int start_after:mode.0?int256 modified_after:mode.2?int = liteServer.ValidatorStats",
    id = 0x091a58bc
) {
    override fun decode(input: Input): LiteServerGetValidatorStats {
        val mode = input.readIntTl()
        val id = input.readTl(TonNodeBlockIdExt)
        val limit = input.readIntTl()
        val startAfter = input.readFlagTl(mode, 0, Int256TlConstructor)
        val modifiedAfter = input.readFlagTl(mode, 2, IntTlConstructor)
        return LiteServerGetValidatorStats(mode, id, limit, startAfter, modifiedAfter)
    }

    override fun encode(output: Output, value: LiteServerGetValidatorStats) {
        output.writeIntTl(value.mode)
        output.writeTl(TonNodeBlockIdExt, value.id)
        output.writeIntTl(value.limit)
        output.writeOptionalTl(value.mode, 0, Int256TlConstructor, value.start_after)
        output.writeOptionalTl(value.mode, 2, IntTlConstructor, value.modified_after)
    }
}
