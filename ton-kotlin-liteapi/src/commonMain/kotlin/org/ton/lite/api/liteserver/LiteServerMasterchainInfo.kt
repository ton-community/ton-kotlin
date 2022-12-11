@file:UseSerializers(HexByteArraySerializer::class)
@file:Suppress("PropertyName", "NOTHING_TO_INLINE")

package org.ton.lite.api.liteserver

import io.ktor.util.*
import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.api.tonnode.TonNodeZeroStateIdExt
import org.ton.crypto.HexByteArraySerializer
import org.ton.crypto.base64.Base64ByteArraySerializer
import org.ton.tl.*
import org.ton.tl.constructors.readInt256Tl
import org.ton.tl.constructors.writeInt256Tl
import kotlin.jvm.JvmStatic

inline fun LiteServerMasterchainInfo(
    last: TonNodeBlockIdExt,
    state_root_hash: ByteArray,
    init: TonNodeZeroStateIdExt
) =
    LiteServerMasterchainInfo.of(last, state_root_hash, init)

interface LiteServerMasterchainInfo : TlObject<LiteServerMasterchainInfo> {
    val last: TonNodeBlockIdExt
    val state_root_hash: ByteArray
    val init: TonNodeZeroStateIdExt

    override fun tlCodec(): TlCodec<LiteServerMasterchainInfo> = Companion

    companion object : TlCodec<LiteServerMasterchainInfo> by LiteServerMasterchainInfoTlbConstructor {
        @JvmStatic
        fun of(
            last: TonNodeBlockIdExt,
            state_root_hash: ByteArray,
            init: TonNodeZeroStateIdExt
        ): LiteServerMasterchainInfo =
            LiteServerMasterchainInfoImpl(last, state_root_hash, init)
    }
}

@Serializable
private data class LiteServerMasterchainInfoImpl(
    override val last: TonNodeBlockIdExt,
    @Serializable(Base64ByteArraySerializer::class)
    override val state_root_hash: ByteArray,
    override val init: TonNodeZeroStateIdExt
) : LiteServerMasterchainInfo {


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LiteServerMasterchainInfoImpl) return false
        if (last != other.last) return false
        if (!state_root_hash.contentEquals(other.state_root_hash)) return false
        if (init != other.init) return false
        return true
    }

    override fun hashCode(): Int {
        var result = last.hashCode()
        result = 31 * result + state_root_hash.contentHashCode()
        result = 31 * result + init.hashCode()
        return result
    }

    override fun toString() = buildString {
        append("(last:")
        append(last)
        append(" state_root_hash:")
        append(hex(state_root_hash).uppercase())
        append(" init:")
        append(init)
        append(")")
    }
}

private object LiteServerMasterchainInfoTlbConstructor : TlConstructor<LiteServerMasterchainInfo>(
    schema = "liteServer.masterchainInfo last:tonNode.blockIdExt state_root_hash:int256 init:tonNode.zeroStateIdExt = liteServer.MasterchainInfo"
) {
    override fun decode(input: Input): LiteServerMasterchainInfo {
        val last = input.readTl(TonNodeBlockIdExt)
        val stateRootHash = input.readInt256Tl()
        val init = input.readTl(TonNodeZeroStateIdExt)
        return LiteServerMasterchainInfoImpl(last, stateRootHash, init)
    }

    override fun encode(output: Output, value: LiteServerMasterchainInfo) {
        output.writeTl(TonNodeBlockIdExt, value.last)
        output.writeInt256Tl(value.state_root_hash)
        output.writeTl(TonNodeZeroStateIdExt, value.init)
    }
}
