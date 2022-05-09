@file:UseSerializers(HexByteArraySerializer::class)

package org.ton.lite.api

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.ton.crypto.HexByteArraySerializer
import org.ton.crypto.hex
import org.ton.tl.TLCodec

@Serializable
data class TonNodeZeroStateIdExt(
        val workchain: Int,
        val rootHash: ByteArray,
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

    override fun toString() =
            "TonNodeZeroStateIdExt(workchain=$workchain, rootHash=${hex(rootHash)}, fileHash=${hex(fileHash)})"

    companion object : TLCodec<TonNodeZeroStateIdExt> {
        override val id: Int = 494024110

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