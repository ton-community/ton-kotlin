package org.ton.lite.api.liteserver

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.tl.TlConstructor
import org.ton.tl.readTl
import org.ton.tl.writeTl

@Serializable
data class LiteServerGetBlock(
    val id: TonNodeBlockIdExt,
) {
    companion object : TlConstructor<LiteServerGetBlock>(
        type = LiteServerGetBlock::class,
        schema = "liteServer.getBlock id:tonNode.blockIdExt = liteServer.BlockData"
    ) {
        override fun decode(input: Input): LiteServerGetBlock {
            val id = input.readTl(TonNodeBlockIdExt)
            return LiteServerGetBlock(id)
        }

        override fun encode(output: Output, value: LiteServerGetBlock) {
            output.writeTl(TonNodeBlockIdExt, value.id)
        }
    }
}
