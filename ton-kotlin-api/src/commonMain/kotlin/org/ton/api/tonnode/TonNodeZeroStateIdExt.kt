@file:UseSerializers(HexByteArraySerializer::class)

package org.ton.api.tonnode

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.ton.crypto.HexByteArraySerializer
import org.ton.crypto.base64.Base64ByteArraySerializer
import org.ton.crypto.encodeHex
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readIntTl
import org.ton.tl.constructors.writeIntTl

@Serializable
data class TonNodeZeroStateIdExt(
    val workchain: Int,
    @Serializable(Base64ByteArraySerializer::class)
    val root_hash: ByteArray,
    @Serializable(Base64ByteArraySerializer::class)
    val file_hash: ByteArray
) {
    constructor() : this(Workchain.INVALID_WORKCHAIN, ByteArray(0), ByteArray(0))
    constructor(tonNodeBlockIdExt: TonNodeBlockIdExt) : this(
        tonNodeBlockIdExt.workchain,
        tonNodeBlockIdExt.root_hash,
        tonNodeBlockIdExt.file_hash
    )

    fun isValid(): Boolean = workchain != Workchain.INVALID_WORKCHAIN

    fun isMasterchain(): Boolean = workchain == Workchain.MASTERCHAIN_ID

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TonNodeZeroStateIdExt) return false
        if (workchain != other.workchain) return false
        if (!root_hash.contentEquals(other.root_hash)) return false
        if (!file_hash.contentEquals(other.file_hash)) return false
        return true
    }

    override fun hashCode(): Int {
        var result = workchain
        result = 31 * result + root_hash.contentHashCode()
        result = 31 * result + file_hash.contentHashCode()
        return result
    }

    override fun toString() = "($workchain:${root_hash.encodeHex().uppercase()}:${file_hash.encodeHex().uppercase()})"

    companion object : TlConstructor<TonNodeZeroStateIdExt>(
        schema = "tonNode.zeroStateIdExt workchain:int root_hash:int256 file_hash:int256 = tonNode.ZeroStateIdExt"
    ) {
        override fun decode(input: Input): TonNodeZeroStateIdExt {
            val workchain = input.readIntTl()
            val rootHash = input.readBytes(32)
            val fileHash = input.readBytes(32)
            return TonNodeZeroStateIdExt(workchain, rootHash, fileHash)
        }

        override fun encode(output: Output, value: TonNodeZeroStateIdExt) {
            output.writeIntTl(value.workchain)
            output.writeFully(value.root_hash)
            output.writeFully(value.file_hash)
        }
    }
}
