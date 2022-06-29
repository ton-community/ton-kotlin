package org.ton.lite.api.liteserver.functions

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.block.VmStack
import org.ton.block.VmStackList
import org.ton.block.VmStackValue
import org.ton.boc.BagOfCells
import org.ton.cell.CellBuilder
import org.ton.crypto.Base64ByteArraySerializer
import org.ton.crypto.crc16
import org.ton.lite.api.liteserver.LiteServerAccountId
import org.ton.lite.api.liteserver.LiteServerRunMethodResult
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.*
import org.ton.tl.readTl
import org.ton.tl.writeTl
import org.ton.tlb.storeTlb

fun interface LiteServerRunSmcMethodFunction : LiteServerQueryFunction {
    suspend fun query(query: LiteServerRunSmcMethod): LiteServerRunMethodResult =
        query(query, LiteServerRunSmcMethod, LiteServerRunMethodResult)

    suspend fun runSmcMethod(
        mode: Int,
        id: TonNodeBlockIdExt,
        account: LiteServerAccountId,
        methodId: Long,
        params: ByteArray
    ) =
        query(LiteServerRunSmcMethod(mode, id, account, methodId, params))

    suspend fun runSmcMethod(
        mode: Int,
        id: TonNodeBlockIdExt,
        account: LiteServerAccountId,
        methodName: String,
        params: BagOfCells
    ) =
        query(LiteServerRunSmcMethod(mode, id, account, methodName, params))

    suspend fun runSmcMethod(
        mode: Int, id: TonNodeBlockIdExt, account: LiteServerAccountId, methodName: String, params: VmStack
    ) = query(LiteServerRunSmcMethod(mode, id, account, methodName, params))

    suspend fun runSmcMethod(
        mode: Int,
        id: TonNodeBlockIdExt,
        account: LiteServerAccountId,
        methodName: String,
        vararg params: VmStackValue
    ) = query(LiteServerRunSmcMethod(mode, id, account, methodName, *params))

    suspend fun runSmcMethod(
        mode: Int,
        id: TonNodeBlockIdExt,
        account: LiteServerAccountId,
        methodName: String,
        params: Iterable<VmStackValue>
    ) = query(LiteServerRunSmcMethod(mode, id, account, methodName, params))
}

@Serializable
data class LiteServerRunSmcMethod(
    val mode: Int,
    val id: TonNodeBlockIdExt,
    val account: LiteServerAccountId,
    val method_id: Long,
    @Serializable(Base64ByteArraySerializer::class)
    val params: ByteArray
) {
    constructor(
        mode: Int, id: TonNodeBlockIdExt, account: LiteServerAccountId, methodName: String, params: BagOfCells
    ) : this(mode, id, account, methodId(methodName), params.toByteArray())

    constructor(
        mode: Int, id: TonNodeBlockIdExt, account: LiteServerAccountId, methodName: String, params: VmStack
    ) : this(mode, id, account, methodName, BagOfCells(CellBuilder.createCell {
        storeTlb(VmStack, params)
    }))

    constructor(
        mode: Int,
        id: TonNodeBlockIdExt,
        account: LiteServerAccountId,
        methodName: String,
        vararg params: VmStackValue
    ) : this(mode, id, account, methodName, VmStack(VmStackList(params.asIterable())))

    constructor(
        mode: Int,
        id: TonNodeBlockIdExt,
        account: LiteServerAccountId,
        methodName: String,
        params: Iterable<VmStackValue>
    ) : this(mode, id, account, methodName, VmStack(VmStackList(params)))

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LiteServerRunSmcMethod

        if (mode != other.mode) return false
        if (id != other.id) return false
        if (account != other.account) return false
        if (method_id != other.method_id) return false
        if (!params.contentEquals(other.params)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = mode
        result = 31 * result + id.hashCode()
        result = 31 * result + account.hashCode()
        result = 31 * result + method_id.hashCode()
        result = 31 * result + params.contentHashCode()
        return result
    }

    companion object : TlCodec<LiteServerRunSmcMethod> by LiteServerRunSmcMethodTlConstructor {
        fun methodId(methodName: String): Long = crc16(methodName).toLong() or 0x10000
    }
}

private object LiteServerRunSmcMethodTlConstructor : TlConstructor<LiteServerRunSmcMethod>(
    type = LiteServerRunSmcMethod::class,
    schema = "liteServer.runSmcMethod mode:# id:tonNode.blockIdExt account:liteServer.accountId method_id:long params:bytes = liteServer.RunMethodResult"
) {
    override fun encode(output: Output, value: LiteServerRunSmcMethod) {
        output.writeIntTl(value.mode)
        output.writeTl(TonNodeBlockIdExt, value.id)
        output.writeTl(LiteServerAccountId, value.account)
        output.writeLongTl(value.method_id)
        output.writeBytesTl(value.params)
    }

    override fun decode(input: Input): LiteServerRunSmcMethod {
        val mode = input.readIntTl()
        val id = input.readTl(TonNodeBlockIdExt)
        val account = input.readTl(LiteServerAccountId)
        val methodId = input.readLongTl()
        val params = input.readBytesTl()
        return LiteServerRunSmcMethod(mode, id, account, methodId, params)
    }
}
