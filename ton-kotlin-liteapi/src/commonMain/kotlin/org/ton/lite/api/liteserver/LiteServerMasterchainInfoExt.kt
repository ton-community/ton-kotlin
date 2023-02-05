package org.ton.lite.api.liteserver

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.api.tonnode.TonNodeZeroStateIdExt
import org.ton.tl.*

@Serializable
@SerialName("liteServer.masterchainInfoExt")
public data class LiteServerMasterchainInfoExt constructor(
    val mode: Int,
    val version: Int,
    val capabilities: Long,
    val last: TonNodeBlockIdExt,
    @SerialName("last_utime")
    val lastUtime: Int,
    val now: Int,
    @SerialName("state_root_hash")
    val stateRootHash: ByteArray,
    val init: TonNodeZeroStateIdExt
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LiteServerMasterchainInfoExt) return false
        if (mode != other.mode) return false
        if (version != other.version) return false
        if (capabilities != other.capabilities) return false
        if (last != other.last) return false
        if (lastUtime != other.lastUtime) return false
        if (now != other.now) return false
        if (!stateRootHash.contentEquals(other.stateRootHash)) return false
        if (init != other.init) return false
        return true
    }

    override fun hashCode(): Int {
        var result = mode
        result = 31 * result + version
        result = 31 * result + capabilities.hashCode()
        result = 31 * result + last.hashCode()
        result = 31 * result + lastUtime
        result = 31 * result + now
        result = 31 * result + stateRootHash.contentHashCode()
        result = 31 * result + init.hashCode()
        return result
    }

    public companion object : TlConstructor<LiteServerMasterchainInfoExt>(
        schema = "liteServer.masterchainInfoExt mode:# version:int capabilities:long last:tonNode.blockIdExt last_utime:int now:int state_root_hash:int256 init:tonNode.zeroStateIdExt = liteServer.MasterchainInfoExt"
    ) {
        override fun decode(reader: TlReader): LiteServerMasterchainInfoExt {
            val mode = reader.readInt()
            val version = reader.readInt()
            val capabilities = reader.readLong()
            val last = reader.read(TonNodeBlockIdExt)
            val lastUtime = reader.readInt()
            val now = reader.readInt()
            val stateRootHash = reader.readRaw(32)
            val init = reader.read(TonNodeZeroStateIdExt)
            return LiteServerMasterchainInfoExt(
                mode,
                version,
                capabilities,
                last,
                lastUtime,
                now,
                stateRootHash,
                init
            )
        }

        override fun encode(writer: TlWriter, value: LiteServerMasterchainInfoExt) {
            writer.writeInt(value.mode)
            writer.writeInt(value.version)
            writer.writeLong(value.capabilities)
            writer.write(TonNodeBlockIdExt, value.last)
            writer.writeInt(value.lastUtime)
            writer.writeInt(value.now)
            writer.writeRaw(value.stateRootHash)
            writer.write(TonNodeZeroStateIdExt, value.init)
        }
    }
}
