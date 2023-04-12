@file:UseSerializers(Base64ByteArraySerializer::class)

package org.ton.lite.api.liteserver

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.crypto.encoding.Base64ByteArraySerializer
import org.ton.tl.*
import kotlin.jvm.JvmName

@Serializable
@SerialName("liteServer.accountState")
public data class LiteServerAccountState(
    @get:JvmName("id")
    val id: TonNodeBlockIdExt,

    @SerialName("shardblk")
    @get:JvmName("shardBlock")
    val shardBlock: TonNodeBlockIdExt,

    @SerialName("shard_proof")
    @get:JvmName("shardProof")
    val shardProof: ByteString,

    @get:JvmName("proof")
    val proof: ByteString,

    @get:JvmName("state")
    val state: ByteString
) {
    public companion object : TlConstructor<LiteServerAccountState>(
        schema = "liteServer.accountState id:tonNode.blockIdExt shardblk:tonNode.blockIdExt shard_proof:bytes proof:bytes state:bytes = liteServer.AccountState"
    ) {
        override fun decode(reader: TlReader): LiteServerAccountState {
            val id = reader.read(TonNodeBlockIdExt)
            val shardBlk = reader.read(TonNodeBlockIdExt)
            val shardProof = reader.readByteString()
            val proof = reader.readByteString()
            val state = reader.readByteString()
            return LiteServerAccountState(id, shardBlk, shardProof, proof, state)
        }

        override fun encode(writer: TlWriter, value: LiteServerAccountState) {
            writer.write(TonNodeBlockIdExt, value.id)
            writer.write(TonNodeBlockIdExt, value.shardBlock)
            writer.writeBytes(value.shardProof)
            writer.writeBytes(value.proof)
            writer.writeBytes(value.state)
        }
    }
}
