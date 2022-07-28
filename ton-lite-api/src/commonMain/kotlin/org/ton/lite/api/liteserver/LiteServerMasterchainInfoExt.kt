package org.ton.lite.api.liteserver

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.api.tonnode.TonNodeZeroStateIdExt
import org.ton.crypto.Base64ByteArraySerializer
import org.ton.crypto.base64
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readInt256Tl
import org.ton.tl.constructors.readIntTl
import org.ton.tl.constructors.writeInt256Tl
import org.ton.tl.constructors.writeIntTl
import org.ton.tl.readTl
import org.ton.tl.writeTl

@Serializable
data class LiteServerMasterchainInfoExt(
    val mode: Int,
    val version: Int,
    val capabilities: Long,
    override val last: TonNodeBlockIdExt,
    val last_utime: Int,
    val now: Int,
    @Serializable(Base64ByteArraySerializer::class)
    override val state_root_hash: ByteArray,
    override val init: TonNodeZeroStateIdExt
) : LiteServerMasterchainInfo {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LiteServerMasterchainInfoExt

        if (mode != other.mode) return false
        if (version != other.version) return false
        if (capabilities != other.capabilities) return false
        if (last != other.last) return false
        if (last_utime != other.last_utime) return false
        if (now != other.now) return false
        if (!state_root_hash.contentEquals(other.state_root_hash)) return false
        if (init != other.init) return false

        return true
    }

    override fun hashCode(): Int {
        var result = mode
        result = 31 * result + version
        result = 31 * result + capabilities.hashCode()
        result = 31 * result + last.hashCode()
        result = 31 * result + last_utime
        result = 31 * result + now
        result = 31 * result + state_root_hash.contentHashCode()
        result = 31 * result + init.hashCode()
        return result
    }

    override fun toString(): String = buildString {
        append("LiteServerMasterchainInfoExt(mode=")
        append(mode)
        append(", version=")
        append(version)
        append(", capabilities=")
        append(capabilities)
        append(", last=")
        append(last)
        append(", lastUTime=")
        append(last_utime)
        append(", now=")
        append(now)
        append(", stateRootHash=")
        append(base64(state_root_hash))
        append(", init=")
        append(init)
        append(")")
    }

    companion object : TlConstructor<LiteServerMasterchainInfoExt>(
        type = LiteServerMasterchainInfoExt::class,
        schema = "liteServer.masterchainInfoExt mode:# version:int capabilities:long last:tonNode.blockIdExt last_utime:int now:int state_root_hash:int256 init:tonNode.zeroStateIdExt = liteServer.MasterchainInfoExt"
    ) {
        override fun encode(output: Output, value: LiteServerMasterchainInfoExt) {
            output.writeIntTl(value.mode)
            output.writeIntTl(value.version)
            output.writeLongLittleEndian(value.capabilities)
            output.writeTl(TonNodeBlockIdExt, value.last)
            output.writeIntTl(value.last_utime)
            output.writeIntTl(value.now)
            output.writeInt256Tl(value.state_root_hash)
            output.writeTl(TonNodeZeroStateIdExt, value.init)
        }

        override fun decode(input: Input): LiteServerMasterchainInfoExt {
            val mode = input.readIntTl()
            val version = input.readIntTl()
            val capabilities = input.readLongLittleEndian()
            val last = input.readTl(TonNodeBlockIdExt)
            val lastUTime = input.readIntTl()
            val now = input.readIntTl()
            val stateRootHash = input.readInt256Tl()
            val init = input.readTl(TonNodeZeroStateIdExt)
            return LiteServerMasterchainInfoExt(mode, version, capabilities, last, lastUTime, now, stateRootHash, init)
        }
    }
}
