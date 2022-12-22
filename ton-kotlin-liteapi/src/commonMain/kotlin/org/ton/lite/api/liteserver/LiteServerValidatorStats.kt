package org.ton.lite.api.liteserver

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.boc.BagOfCells
import org.ton.lite.api.liteserver.internal.readBoc
import org.ton.lite.api.liteserver.internal.writeBoc
import org.ton.tl.*
import org.ton.tl.constructors.*

@Serializable
public data class LiteServerValidatorStats(
    val mode: Int,
    val id: TonNodeBlockIdExt,
    val count: Int,
    val complete: Boolean,
    @SerialName("state_proof")
    val stateProofBoc: BagOfCells,
    @SerialName("data_proof")
    val dataProofBoc: BagOfCells
) {
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
        val stateProofBoc = reader.readBoc()
        val dataProofBoc = reader.readBoc()
        return LiteServerValidatorStats(mode, id, count, complete, stateProofBoc, dataProofBoc)
    }

    override fun encode(writer: TlWriter, value: LiteServerValidatorStats) {
        writer.writeInt(value.mode)
        writer.write(TonNodeBlockIdExt, value.id)
        writer.writeInt(value.count)
        writer.writeBoolean(value.complete)
        writer.writeBoc(value.stateProofBoc)
        writer.writeBoc(value.dataProofBoc)
    }
}
