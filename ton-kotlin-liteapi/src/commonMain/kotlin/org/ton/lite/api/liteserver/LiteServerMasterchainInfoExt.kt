package org.ton.lite.api.liteserver

import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.api.tonnode.TonNodeZeroStateIdExt
import org.ton.bitstring.BitString
import org.ton.tl.*

@Serializable
public data class LiteServerMasterchainInfoExt constructor(
    val mode: Int,
    val version: Int,
    val capabilities: Long,
    val last: TonNodeBlockIdExt,
    val lastUtime: Int,
    val now: Int,
    val stateRootHash: Bits256,
    val init: TonNodeZeroStateIdExt
) {

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
            val stateRootHash = reader.readBits256()
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
            writer.writeBits256(value.stateRootHash)
        }
    }
}
