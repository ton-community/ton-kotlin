package ton.lite.client

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import ton.adnl.TLCodec
import ton.crypto.hex
import ton.types.util.HexByteArraySerializer

data class LiteServerMasterchainInfo(
    val last: TonNodeBlockIdExt,
    @Serializable(HexByteArraySerializer::class)
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

    override fun toString() =
        "LiteServerMasterchainInfo(last=$last, stateRootHash=${hex(stateRootHash)}, init=$init)"

    companion object : TLCodec<LiteServerMasterchainInfo> {
        override val id: Int = -2055001983

        override fun decode(input: Input): LiteServerMasterchainInfo {
            val last = input.readTl(TonNodeBlockIdExt)
            val stateRootHash = input.readBytes(32)
            val init = input.readTl(TonNodeZeroStateIdExt)
            return LiteServerMasterchainInfo(last, stateRootHash, init)
        }

        override fun encode(output: Output, message: LiteServerMasterchainInfo) {
            output.writeTl(message.last, TonNodeBlockIdExt)
            output.writeFully(message.stateRootHash)
            output.writeTl(message.init, TonNodeZeroStateIdExt)
        }
    }
}