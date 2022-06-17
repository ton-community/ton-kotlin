@file:UseSerializers(HexByteArraySerializer::class)

package org.ton.lite.api.liteserver

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.api.tonnode.TonNodeZeroStateIdExt
import org.ton.crypto.Base64ByteArraySerializer
import org.ton.crypto.HexByteArraySerializer
import org.ton.crypto.base64
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readInt256Tl
import org.ton.tl.constructors.writeInt256Tl
import org.ton.tl.readTl
import org.ton.tl.writeTl

@Serializable
data class LiteServerMasterchainInfo(
        val last: TonNodeBlockIdExt,
        @SerialName("state_root_hash")
        @Serializable(Base64ByteArraySerializer::class)
        val stateRootHash: ByteArray,
        val init: TonNodeZeroStateIdExt
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LiteServerMasterchainInfo

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

    override fun toString() = buildString {
        append("LiteServerMasterchainInfo(last=")
        append(last)
        append(", stateRootHash=")
        append(base64(stateRootHash))
        append(", init=")
        append(init)
        append(")")
    }

    companion object : TlConstructor<LiteServerMasterchainInfo>(
            type = LiteServerMasterchainInfo::class,
            schema = "liteServer.masterchainInfo last:tonNode.blockIdExt state_root_hash:int256 init:tonNode.zeroStateIdExt = liteServer.MasterchainInfo"
    ) {
        override fun decode(input: Input): LiteServerMasterchainInfo {
            val last = input.readTl(TonNodeBlockIdExt)
            val stateRootHash = input.readInt256Tl()
            val init = input.readTl(TonNodeZeroStateIdExt)
            return LiteServerMasterchainInfo(last, stateRootHash, init)
        }

        override fun encode(output: Output, value: LiteServerMasterchainInfo) {
            output.writeTl(TonNodeBlockIdExt, value.last)
            output.writeInt256Tl(value.stateRootHash)
            output.writeTl(TonNodeZeroStateIdExt, value.init)
        }
    }
}
