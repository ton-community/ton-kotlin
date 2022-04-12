package ton.lite.client

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import ton.adnl.TLCodec
import ton.crypto.hex
import ton.types.util.HexByteArraySerializer

@Serializable
data class TonNodeBlockIdExt(
    val id: TonNodeBlockId,
    @Serializable(HexByteArraySerializer::class)
    val rootHash: ByteArray,
    @Serializable(HexByteArraySerializer::class)
    val fileHash: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TonNodeBlockIdExt

        if (id != other.id) return false
        if (!rootHash.contentEquals(other.rootHash)) return false
        if (!fileHash.contentEquals(other.fileHash)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + rootHash.contentHashCode()
        result = 31 * result + fileHash.contentHashCode()
        return result
    }

    override fun toString() =
        "TonNodeBlockIdExt(id=$id, rootHash=${hex(rootHash)}, fileHash=${hex(fileHash)})"

    companion object : TLCodec<TonNodeBlockIdExt> {
        override val id: Int = 1733487480

        override fun decode(input: Input): TonNodeBlockIdExt {
            val id = input.readTl(TonNodeBlockId)
            val rootHash = input.readBytes(32)
            val fileHash = input.readBytes(32)
            return TonNodeBlockIdExt(id, rootHash, fileHash)
        }

        override fun encode(output: Output, message: TonNodeBlockIdExt) {
            output.writeTl(message.id, TonNodeBlockId)
            output.writeFully(message.rootHash)
            output.writeFully(message.fileHash)
        }
    }
}