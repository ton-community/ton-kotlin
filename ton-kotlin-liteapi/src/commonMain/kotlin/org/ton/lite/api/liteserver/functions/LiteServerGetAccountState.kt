package org.ton.lite.api.liteserver.functions

import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.block.AddrStd
import org.ton.lite.api.liteserver.LiteServerAccountId
import org.ton.lite.api.liteserver.LiteServerAccountState
import org.ton.tl.*

@Serializable
public data class LiteServerGetAccountState(
    val id: TonNodeBlockIdExt,
    val account: LiteServerAccountId,
) : TLFunction<LiteServerGetAccountState, LiteServerAccountState> {
    public constructor(
        id: TonNodeBlockIdExt,
        account: AddrStd
    ) : this(id, LiteServerAccountId(account))

    override fun tlCodec(): TlCodec<LiteServerGetAccountState> = Companion

    override fun resultTlCodec(): TlCodec<LiteServerAccountState> = LiteServerAccountState

    public companion object : TlConstructor<LiteServerGetAccountState>(
        schema = "liteServer.getAccountState id:tonNode.blockIdExt account:liteServer.accountId = liteServer.AccountState"
    ) {
        override fun decode(reader: TlReader): LiteServerGetAccountState {
            val id = reader.read(TonNodeBlockIdExt)
            val account = reader.read(LiteServerAccountId)
            return LiteServerGetAccountState(id, account)
        }

        override fun encode(writer: TlWriter, value: LiteServerGetAccountState) {
            writer.write(TonNodeBlockIdExt, value.id)
            writer.write(LiteServerAccountId, value.account)
        }
    }
}
