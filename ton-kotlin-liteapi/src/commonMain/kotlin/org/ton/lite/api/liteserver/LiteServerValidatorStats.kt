package org.ton.lite.api.liteserver

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.tl.*

@Serializable
@SerialName("liteServer.validatorStats")
public data class LiteServerValidatorStats(
    val mode: Int,
    val id: TonNodeBlockIdExt,
    val count: Int,
    val complete: Boolean,
    @SerialName("state_proof")
    val stateProof: ByteArray,
    @SerialName("data_proof")
    val dataProof: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LiteServerValidatorStats) return false

        if (mode != other.mode) return false
        if (id != other.id) return false
        if (count != other.count) return false
        if (complete != other.complete) return false
        if (!stateProof.contentEquals(other.stateProof)) return false
        if (!dataProof.contentEquals(other.dataProof)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = mode
        result = 31 * result + id.hashCode()
        result = 31 * result + count
        result = 31 * result + complete.hashCode()
        result = 31 * result + stateProof.contentHashCode()
        result = 31 * result + dataProof.contentHashCode()
        return result
    }

    public companion object : TlCodec<LiteServerValidatorStats> by LiteServerValidatorStatsTlConstructor
}

private object LiteServerValidatorStatsTlConstructor : TlConstructor<LiteServerValidatorStats>(
    schema = "liteServer.validatorStats mode:# id:tonNode.blockIdExt count:int complete:Bool state_proof:bytes data_proof:bytes = liteServer.ValidatorStats"
) {
    override fun decode(reader: TlReader): LiteServerValidatorStats {
        val mode = reader.readInt()
        val id = reader.read(TonNodeBlockIdExt)
        val count = reader.readInt()
        val complete = reader.readBoolean()
        val stateProofBoc = reader.readBytes()
        val dataProofBoc = reader.readBytes()
        return LiteServerValidatorStats(mode, id, count, complete, stateProofBoc, dataProofBoc)
    }

    override fun encode(writer: TlWriter, value: LiteServerValidatorStats) {
        writer.writeInt(value.mode)
        writer.write(TonNodeBlockIdExt, value.id)
        writer.writeInt(value.count)
        writer.writeBoolean(value.complete)
        writer.writeBytes(value.stateProof)
        writer.writeBytes(value.dataProof)
    }
}
