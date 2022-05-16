package org.ton.lite.api.liteserver

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.tl.TlConstructor
import org.ton.tl.readTl
import org.ton.tl.writeTl

@Serializable
data class LiteServerGetAccountState(
        val id: TonNodeBlockIdExt,
        val account: LiteServerAccountId,
) {
    companion object : TlConstructor<LiteServerGetAccountState>(
            type = LiteServerGetAccountState::class,
            schema = "liteServer.getAccountState id:tonNode.blockIdExt account:liteServer.accountId = liteServer.AccountState"
    ) {
        override fun decode(input: Input): LiteServerGetAccountState {
            val id = input.readTl(TonNodeBlockIdExt)
            val account = input.readTl(LiteServerAccountId)
            return LiteServerGetAccountState(id, account)
        }

        override fun encode(output: Output, value: LiteServerGetAccountState) {
            output.writeTl(value.id, TonNodeBlockIdExt)
            output.writeTl(value.account, LiteServerAccountId)
        }
    }
}