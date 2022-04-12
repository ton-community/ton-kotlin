package ton.lite.client

import io.ktor.utils.io.core.*
import ton.adnl.TLCodec

@kotlinx.serialization.Serializable
data class LiteServerGetAccountState(
    val id: TonNodeBlockIdExt,
    val account: LiteServerAccountId,
) {
    companion object : TLCodec<LiteServerGetAccountState> {
        override val id: Int = 1804144165

        override fun decode(input: Input): LiteServerGetAccountState {
            val id = input.readTl(TonNodeBlockIdExt)
            val account = input.readTl(LiteServerAccountId)
            return LiteServerGetAccountState(id, account)
        }

        override fun encode(output: Output, message: LiteServerGetAccountState) {
            output.writeTl(message.id, TonNodeBlockIdExt)
            output.writeTl(message.account, LiteServerAccountId)
        }
    }
}