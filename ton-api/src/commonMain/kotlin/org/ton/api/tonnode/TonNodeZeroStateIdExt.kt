@file:UseSerializers(HexByteArraySerializer::class)

package org.ton.api.tonnode

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.ton.crypto.Base64ByteArraySerializer
import org.ton.crypto.HexByteArraySerializer
import org.ton.crypto.base64
import org.ton.tl.TlConstructor

@Serializable
data class TonNodeZeroStateIdExt(
        val workchain: Int,
        @SerialName("root_hash")
        @Serializable(Base64ByteArraySerializer::class)
        val rootHash: ByteArray,
        @SerialName("file_hash")
        @Serializable(Base64ByteArraySerializer::class)
        val fileHash: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TonNodeZeroStateIdExt

        if (workchain != other.workchain) return false
        if (!rootHash.contentEquals(other.rootHash)) return false
        if (!fileHash.contentEquals(other.fileHash)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = workchain
        result = 31 * result + rootHash.contentHashCode()
        result = 31 * result + fileHash.contentHashCode()
        return result
    }

    override fun toString() = buildString {
        append("TonNodeZeroStateIdExt(workchain=")
        append(workchain)
        append(", rootHash=")
        append(base64(rootHash))
        append(", fileHash=")
        append(base64(fileHash))
        append(")")
    }

    companion object : TlConstructor<TonNodeZeroStateIdExt>(
            type = TonNodeZeroStateIdExt::class,
            schema = "tonNode.zeroStateIdExt workchain:int root_hash:int256 file_hash:int256 = tonNode.ZeroStateIdExt"
    ) {
        override fun decode(input: Input): TonNodeZeroStateIdExt {
            val workchain = input.readIntLittleEndian()
            val rootHash = input.readBytes(32)
            val fileHash = input.readBytes(32)
            return TonNodeZeroStateIdExt(workchain, rootHash, fileHash)
        }

        override fun encode(output: Output, message: TonNodeZeroStateIdExt) {
            output.writeIntLittleEndian(message.workchain)
            output.writeFully(message.rootHash)
            output.writeFully(message.fileHash)
        }
    }
}