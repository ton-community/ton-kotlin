package org.ton.lite.api.liteserver

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.bitstring.BitString
import org.ton.bitstring.toBitString
import org.ton.boc.BagOfCells
import org.ton.lite.api.liteserver.internal.readBoc
import org.ton.lite.api.liteserver.internal.writeBoc
import org.ton.tl.*

@Serializable
public data class LiteServerBlockState(
    val id: TonNodeBlockIdExt,
    @SerialName("root_hash")
    val rootHash: Bits256,
    @SerialName("file_hash")
    val fileHash: Bits256,
    val data: BagOfCells
) {
    public constructor(
        id: TonNodeBlockIdExt,
        rootHash: ByteArray,
        fileHash: ByteArray,
        data: BagOfCells
    ) : this(id, Bits256(rootHash), Bits256(fileHash), data)

    public companion object : TlCodec<LiteServerBlockState> by LiteServerBlockStateTlConstructor
}

private object LiteServerBlockStateTlConstructor : TlConstructor<LiteServerBlockState>(
    schema = "liteServer.blockState id:tonNode.blockIdExt root_hash:int256 file_hash:int256 data:bytes = liteServer.BlockState"
) {
    override fun decode(reader: TlReader): LiteServerBlockState {
        val id = reader.read(TonNodeBlockIdExt)
        val rootHash = reader.readBits256()
        val fileHash = reader.readBits256()
        val data = reader.readBoc()
        return LiteServerBlockState(id, rootHash, fileHash, data)
    }

    override fun encode(writer: TlWriter, value: LiteServerBlockState) {
        writer.write(TonNodeBlockIdExt, value.id)
        writer.writeBits256(value.rootHash)
        writer.writeBits256(value.fileHash)
        writer.writeBoc(value.data)
    }
}
