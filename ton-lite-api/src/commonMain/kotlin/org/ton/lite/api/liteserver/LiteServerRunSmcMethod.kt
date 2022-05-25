package org.ton.lite.api.liteserver

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.block.VmStack
import org.ton.block.tlb.tlbCodec
import org.ton.cell.BagOfCells
import org.ton.cell.CellBuilder
import org.ton.crypto.Base64ByteArraySerializer
import org.ton.crypto.crc16
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.*
import org.ton.tl.readTl
import org.ton.tl.writeTl
import org.ton.tlb.storeTlb

@Serializable
data class LiteServerRunSmcMethod(
    val mode: Int,
    val id: TonNodeBlockIdExt,
    val account: LiteServerAccountId,
    @SerialName("method_id")
    val methodId: Long,
    @Serializable(Base64ByteArraySerializer::class)
    val params: ByteArray
) {
    constructor(
        mode: Int,
        id: TonNodeBlockIdExt,
        account: LiteServerAccountId,
        methodName: String,
        params: BagOfCells
    ) : this(mode, id, account, methodId(methodName), params.toByteArray())

    constructor(
        mode: Int,
        id: TonNodeBlockIdExt,
        account: LiteServerAccountId,
        methodName: String,
        params: VmStack
    ) : this(mode, id, account, methodName, BagOfCells(CellBuilder.createCell { storeTlb(params, vmStackCodec) }))

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LiteServerRunSmcMethod

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

    companion object : TlConstructor<LiteServerRunSmcMethod>(
        type = LiteServerRunSmcMethod::class,
        schema = "liteServer.runSmcMethod mode:# id:tonNode.blockIdExt account:liteServer.accountId method_id:long params:bytes = liteServer.RunMethodResult"
    ) {
        private val vmStackCodec by lazy { VmStack.tlbCodec() }

        fun methodId(methodName: String): Long = crc16(methodName).toLong() or 0x10000

        override fun encode(output: Output, value: LiteServerRunSmcMethod) {
            output.writeIntTl(value.mode)
            output.writeTl(value.id, TonNodeBlockIdExt)
            output.writeTl(value.account, LiteServerAccountId)
            output.writeLongTl(value.methodId)
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
}
