package org.ton.api.tonnode

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readIntTl
import org.ton.tl.constructors.writeIntTl

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
            val workchain = input.readIntTl()
            val shard = input.readLongLittleEndian()
            val seqno = input.readIntTl()
            return TonNodeBlockId(workchain, shard, seqno)
        }

        override fun encode(output: Output, value: TonNodeBlockId) {
            output.writeIntTl(value.workchain)
            output.writeLongLittleEndian(value.shard)
            output.writeIntTl(value.seqno)
        }
    }
}