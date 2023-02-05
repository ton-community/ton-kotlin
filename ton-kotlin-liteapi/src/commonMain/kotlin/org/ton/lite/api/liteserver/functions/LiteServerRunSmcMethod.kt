package org.ton.lite.api.liteserver.functions

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.block.VmStack
import org.ton.block.VmStackList
import org.ton.block.VmStackValue
import org.ton.boc.BagOfCells
import org.ton.crypto.crc16
import org.ton.lite.api.liteserver.LiteServerAccountId
import org.ton.lite.api.liteserver.LiteServerRunMethodResult
import org.ton.tl.*
import kotlin.jvm.JvmStatic

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
        @JvmStatic
        public fun methodId(methodName: String): Long = crc16(methodName).toLong() or 0x10000

        @JvmStatic
        public fun params(vmStack: VmStack): ByteArray =
            BagOfCells(VmStack.createCell(vmStack)).toByteArray()

        @JvmStatic
        public fun params(vmStackList: VmStackList?): ByteArray =
            params(vmStack = VmStack(vmStackList ?: VmStackList()))

        @JvmStatic
        public fun params(params: Iterable<VmStackValue>): ByteArray =
            params(vmStackList = VmStackList(params))

        @JvmStatic
        public fun params(vararg params: VmStackValue): ByteArray =
            params(params.asIterable())
    }
}

private object LiteServerRunSmcMethodTlConstructor : TlConstructor<LiteServerRunSmcMethod>(
    schema = "liteServer.runSmcMethod mode:# id:tonNode.blockIdExt account:liteServer.accountId method_id:long params:bytes = liteServer.RunMethodResult"
) {
    override fun encode(writer: TlWriter, value: LiteServerRunSmcMethod) {
        writer.writeInt(value.mode)
        writer.write(TonNodeBlockIdExt, value.id)
        writer.write(LiteServerAccountId, value.account)
        writer.writeLong(value.methodId)
        writer.writeBytes(value.params)
    }

    override fun decode(reader: TlReader): LiteServerRunSmcMethod {
        val mode = reader.readInt()
        val id = reader.read(TonNodeBlockIdExt)
        val account = reader.read(LiteServerAccountId)
        val methodId = reader.readLong()
        val params = reader.readBytes()
        return LiteServerRunSmcMethod(mode, id, account, methodId, params)
    }
}
