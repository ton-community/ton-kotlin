package org.ton.lite.api.liteserver

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readBytesTl
import org.ton.tl.constructors.readInt256Tl
import org.ton.tl.constructors.writeBytesTl
import org.ton.tl.constructors.writeInt256Tl
import org.ton.tl.readTl
import org.ton.tl.writeTl

@Serializable
class LiteServerBlockState(
    val id: TonNodeBlockIdExt,
    val root_hash: ByteArray,
    val file_hash: ByteArray,
    val data: ByteArray
) {
    companion object : TlCodec<LiteServerBlockState> by LiteServerBlockStateTlConstructor
}

private object LiteServerBlockStateTlConstructor : TlConstructor<LiteServerBlockState>(
    schema = "liteServer.blockState id:tonNode.blockIdExt root_hash:int256 file_hash:int256 data:bytes = liteServer.BlockState"
) {
    override fun decode(input: Input): LiteServerBlockState {
        val id = input.readTl(TonNodeBlockIdExt)
        val rootHash = input.readInt256Tl()
        val fileHash = input.readInt256Tl()
        val data = input.readBytesTl()
        return LiteServerBlockState(id, rootHash, fileHash, data)
    }

    override fun encode(output: Output, value: LiteServerBlockState) {
        output.writeTl(TonNodeBlockIdExt, value.id)
        output.writeInt256Tl(value.root_hash)
        output.writeInt256Tl(value.file_hash)
        output.writeBytesTl(value.data)
    }
}
