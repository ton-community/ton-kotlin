@file:UseSerializers(HexByteArraySerializer::class)
@file:Suppress("PropertyName", "NOTHING_TO_INLINE")

package org.ton.lite.api.liteserver

import io.ktor.util.*
import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.api.tonnode.TonNodeZeroStateIdExt
import org.ton.crypto.HexByteArraySerializer
import org.ton.tl.*

@Serializable
@SerialName("liteServer.masterchainInfo")
public data class LiteServerMasterchainInfo(
    public val last: TonNodeBlockIdExt,
    public val stateRootHash: ByteArray,
    public val init: TonNodeZeroStateIdExt
) {
    init {
        require(stateRootHash.size == 32) { "Invalid stateRootHash size: ${stateRootHash.size}, expected: 32" }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LiteServerMasterchainInfo) return false
        if (last != other.last) return false
        if (!stateRootHash.contentEquals(other.stateRootHash)) return false
        if (init != other.init) return false
        return true
    }

    override fun hashCode(): Int {
        var result = last.hashCode()
        result = 31 * result + stateRootHash.contentHashCode()
        result = 31 * result + init.hashCode()
        return result
    }

    public companion object : TlCodec<LiteServerMasterchainInfo> by LiteServerMasterchainInfoTlbConstructor
}

private object LiteServerMasterchainInfoTlbConstructor : TlConstructor<LiteServerMasterchainInfo>(
    schema = "liteServer.masterchainInfo last:tonNode.blockIdExt state_root_hash:int256 init:tonNode.zeroStateIdExt = liteServer.MasterchainInfo"
) {
    override fun decode(reader: TlReader): LiteServerMasterchainInfo {
        val last = reader.read(TonNodeBlockIdExt)
        val stateRootHash = reader.readRaw(32)
        val init = reader.read(TonNodeZeroStateIdExt)
        return LiteServerMasterchainInfo(last, stateRootHash, init)
    }

    override fun encode(writer: TlWriter, value: LiteServerMasterchainInfo) {
        writer.write(TonNodeBlockIdExt, value.last)
        writer.writeRaw(value.stateRootHash)
        writer.write(TonNodeZeroStateIdExt, value.init)
    }
}
