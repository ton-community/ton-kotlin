package org.ton.lite.api.liteserver

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.crypto.encodeHex
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.*
import org.ton.tl.readTl
import org.ton.tl.writeTl

@Serializable
data class LiteServerValidatorStats(
    val mode: Int,
    val id: TonNodeBlockIdExt,
    val count: Int,
    val complete: Boolean,
    val state_proof: ByteArray,
    val data_proof: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LiteServerValidatorStats) return false
        if (mode != other.mode) return false
        if (id != other.id) return false
        if (count != other.count) return false
        if (complete != other.complete) return false
        if (!state_proof.contentEquals(other.state_proof)) return false
        if (!data_proof.contentEquals(other.data_proof)) return false
        return true
    }

    override fun hashCode(): Int {
        var result = mode
        result = 31 * result + id.hashCode()
        result = 31 * result + count
        result = 31 * result + complete.hashCode()
        result = 31 * result + state_proof.contentHashCode()
        result = 31 * result + data_proof.contentHashCode()
        return result
    }

    override fun toString(): String = buildString {
        append("LiteServerValidatorStats(mode=")
        append(mode)
        append(", id=")
        append(id)
        append(", count=")
        append(count)
        append(", complete=")
        append(complete)
        append(", state_proof=")
        append(state_proof.encodeHex())
        append(", data_proof=")
        append(data_proof.encodeHex())
        append(")")
    }

    companion object : TlCodec<LiteServerValidatorStats> by LiteServerValidatorStatsTlConstructor
}

private object LiteServerValidatorStatsTlConstructor : TlConstructor<LiteServerValidatorStats>(
    schema = "liteServer.validatorStats mode:# id:tonNode.blockIdExt count:int complete:Bool state_proof:bytes data_proof:bytes = liteServer.ValidatorStats"
) {
    override fun decode(input: Input): LiteServerValidatorStats {
        val mode = input.readIntTl()
        val id = input.readTl(TonNodeBlockIdExt)
        val count = input.readIntTl()
        val complete = input.readBoolTl()
        val stateProof = input.readBytesTl()
        val dataProof = input.readBytesTl()
        return LiteServerValidatorStats(mode, id, count, complete, stateProof, dataProof)
    }

    override fun encode(output: Output, value: LiteServerValidatorStats) {
        output.writeIntTl(value.mode)
        output.writeTl(TonNodeBlockIdExt, value.id)
        output.writeIntTl(value.count)
        output.writeBoolTl(value.complete)
        output.writeBytesTl(value.state_proof)
        output.writeBytesTl(value.data_proof)
    }
}
