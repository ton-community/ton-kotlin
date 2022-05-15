package org.ton.api.tonnode

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.tl.TlConstructor

@Serializable
data class TonNodeBlockId(
        val workchain: Int,
        val shard: Long,
        val seqno: Int
) {
    companion object : TlConstructor<TonNodeBlockId>(
            type = TonNodeBlockId::class,
            schema = "tonNode.blockId workchain:int shard:long seqno:int = tonNode.BlockId"
    ) {
        override fun decode(input: Input): TonNodeBlockId {
            val workchain = input.readIntLittleEndian()
            val shard = input.readLongLittleEndian()
            val seqno = input.readIntLittleEndian()
            return TonNodeBlockId(workchain, shard, seqno)
        }

        override fun encode(output: Output, message: TonNodeBlockId) {
            output.writeIntLittleEndian(message.workchain)
            output.writeLongLittleEndian(message.shard)
            output.writeIntLittleEndian(message.seqno)
        }
    }
}