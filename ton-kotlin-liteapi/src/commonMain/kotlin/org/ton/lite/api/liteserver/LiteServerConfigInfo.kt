package org.ton.lite.api.liteserver

import io.ktor.utils.io.core.*
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.crypto.encodeHex
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readBytesTl
import org.ton.tl.constructors.readIntTl
import org.ton.tl.constructors.writeBytesTl
import org.ton.tl.constructors.writeIntTl
import org.ton.tl.readTl
import org.ton.tl.writeTl

data class LiteServerConfigInfo(
    val mode: Int,
    val id: TonNodeBlockIdExt,
    val state_proof: ByteArray,
    val config_proof: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LiteServerConfigInfo) return false
        if (mode != other.mode) return false
        if (id != other.id) return false
        if (!state_proof.contentEquals(other.state_proof)) return false
        if (!config_proof.contentEquals(other.config_proof)) return false
        return true
    }

    override fun hashCode(): Int {
        var result = mode
        result = 31 * result + id.hashCode()
        result = 31 * result + state_proof.contentHashCode()
        result = 31 * result + config_proof.contentHashCode()
        return result
    }

    override fun toString(): String = buildString {
        append("LiteServerConfigInfo(mode=")
        append(mode)
        append(", id=")
        append(id)
        append(", state_proof=")
        append(state_proof.encodeHex())
        append(", config_proof=")
        append(config_proof.encodeHex())
        append(")")
    }

    companion object : TlCodec<LiteServerConfigInfo> by LiteServerConfigInfoTlConstructor
}

private object LiteServerConfigInfoTlConstructor : TlConstructor<LiteServerConfigInfo>(
    schema = "liteServer.configInfo mode:# id:tonNode.blockIdExt state_proof:bytes config_proof:bytes = liteServer.ConfigInfo"
) {
    override fun decode(input: Input): LiteServerConfigInfo {
        val mode = input.readIntTl()
        val id = input.readTl(TonNodeBlockIdExt)
        val stateProof = input.readBytesTl()
        val configProof = input.readBytesTl()
        return LiteServerConfigInfo(mode, id, stateProof, configProof)
    }

    override fun encode(output: Output, value: LiteServerConfigInfo) {
        output.writeIntTl(value.mode)
        output.writeTl(TonNodeBlockIdExt, value.id)
        output.writeBytesTl(value.state_proof)
        output.writeBytesTl(value.config_proof)
    }
}
