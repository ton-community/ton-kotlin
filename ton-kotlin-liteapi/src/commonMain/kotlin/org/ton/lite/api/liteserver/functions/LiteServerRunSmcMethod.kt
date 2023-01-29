package org.ton.lite.api.liteserver.functions

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.lite.api.liteserver.LiteServerAccountId
import org.ton.lite.api.liteserver.LiteServerRunMethodResult
import org.ton.tl.*

@Serializable
@SerialName("liteServer.runSmcMethod")
public data class LiteServerRunSmcMethod(
    val mode: Int,
    val id: TonNodeBlockIdExt,
    val account: LiteServerAccountId,
    @SerialName("method_id")
    val methodId: Long,
    val params: ByteArray
) : TLFunction<LiteServerRunSmcMethod, LiteServerRunMethodResult> {
    override fun tlCodec(): TlCodec<LiteServerRunSmcMethod> = LiteServerRunSmcMethod
    override fun resultTlCodec(): TlCodec<LiteServerRunMethodResult> = LiteServerRunMethodResult

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LiteServerRunSmcMethod) return false

        if (mode != other.mode) return false
        if (id != other.id) return false
        if (account != other.account) return false
        if (methodId != other.methodId) return false
        if (!params.contentEquals(other.params)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = mode
        result = 31 * result + id.hashCode()
        result = 31 * result + account.hashCode()
        result = 31 * result + methodId.hashCode()
        result = 31 * result + params.contentHashCode()
        return result
    }

    public companion object : TlCodec<LiteServerRunSmcMethod> by LiteServerRunSmcMethodTlConstructor {

    }
}

private object LiteServerRunSmcMethodTlConstructor : TlConstructor<LiteServerRunSmcMethod>(
    schema = "liteServer.runSmcMethod mode:# id:tonNode.blockIdExt account:liteServer.accountId method_id:long params:bytes = liteServer.RunMethodResult"
) {
    override fun encode(output: TlWriter, value: LiteServerRunSmcMethod) {
        output.writeInt(value.mode)
        output.write(TonNodeBlockIdExt, value.id)
        output.write(LiteServerAccountId, value.account)
        output.writeLong(value.methodId)
        output.writeBytes(value.params)
    }

    override fun decode(input: TlReader): LiteServerRunSmcMethod {
        val mode = input.readInt()
        val id = input.read(TonNodeBlockIdExt)
        val account = input.read(LiteServerAccountId)
        val methodId = input.readLong()
        val params = input.readBytes()
        return LiteServerRunSmcMethod(mode, id, account, methodId, params)
    }
}
