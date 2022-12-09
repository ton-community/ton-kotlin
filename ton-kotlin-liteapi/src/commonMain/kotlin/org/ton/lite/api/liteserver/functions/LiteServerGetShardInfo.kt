package org.ton.lite.api.liteserver.functions

import io.ktor.utils.io.core.*
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.lite.api.liteserver.LiteServerShardInfo
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.*
import org.ton.tl.readTl
import org.ton.tl.writeTl

interface LiteServerGetShardInfoFunction : LiteServerQueryFunction {
    suspend fun query(query: LiteServerGetShardInfo): LiteServerShardInfo =
        query(query, LiteServerGetShardInfo, LiteServerShardInfo)

    suspend fun getShardInfo(
        id: TonNodeBlockIdExt,
        workchain: Int,
        shard: Long,
        exact: Boolean
    ) = query(LiteServerGetShardInfo(id, workchain, shard, exact))
}

data class LiteServerGetShardInfo(
    val id: TonNodeBlockIdExt,
    val workchain: Int,
    val shard: Long,
    val exact: Boolean
) {
    companion object : TlCodec<LiteServerGetShardInfo> by LiteServerGetShardInfoTlConstructor
}

private object LiteServerGetShardInfoTlConstructor : TlConstructor<LiteServerGetShardInfo>(
    schema = "liteServer.getShardInfo id:tonNode.blockIdExt workchain:int shard:long exact:Bool = liteServer.ShardInfo"
) {
    override fun decode(input: Input): LiteServerGetShardInfo {
        val id = input.readTl(TonNodeBlockIdExt)
        val workchain = input.readIntTl()
        val shard = input.readLongTl()
        val exact = input.readBoolTl()
        return LiteServerGetShardInfo(id, workchain, shard, exact)
    }

    override fun encode(output: Output, value: LiteServerGetShardInfo) {
        output.writeTl(TonNodeBlockIdExt, value.id)
        output.writeIntTl(value.workchain)
        output.writeLongTl(value.shard)
        output.writeBoolTl(value.exact)
    }
}
