package org.ton.lite.api.liteserver.functions

import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.block.VmStack
import org.ton.block.VmStackList
import org.ton.block.VmStackValue
import org.ton.boc.BagOfCells
import org.ton.cell.CellBuilder
import org.ton.crypto.crc16
import org.ton.lite.api.liteserver.LiteServerAccountId
import org.ton.lite.api.liteserver.LiteServerRunMethodResult
import org.ton.lite.api.liteserver.internal.readBoc
import org.ton.lite.api.liteserver.internal.writeBoc
import org.ton.tl.*
import org.ton.tlb.CellRef
import org.ton.tlb.storeTlb
import kotlin.jvm.JvmStatic

@Serializable
public data class LiteServerRunSmcMethod(
    val mode: Int,
    val id: TonNodeBlockIdExt,
    val account: LiteServerAccountId,
    val methodId: Long,
    val params: BagOfCells = createParams()
) : TLFunction<LiteServerRunSmcMethod, LiteServerRunMethodResult> {
    public constructor(
        mode: Int,
        id: TonNodeBlockIdExt,
        account: LiteServerAccountId,
        methodName: String,
        params: BagOfCells = createParams()
    ) : this(mode, id, account, methodId(methodName), params)

    public constructor(
        mode: Int, id: TonNodeBlockIdExt, account: LiteServerAccountId, methodId: Long, params: VmStack
    ) : this(mode, id, account, methodId, createParams(params))

    public constructor(
        mode: Int, id: TonNodeBlockIdExt, account: LiteServerAccountId, methodName: String, params: VmStack
    ) : this(mode, id, account, methodName, createParams(params))

    public fun paramsAsVmStack(): CellRef<VmStack> = CellRef(params.first(), VmStack)

    override fun tlCodec(): TlCodec<LiteServerRunSmcMethod> = LiteServerRunSmcMethod
    override fun resultTlCodec(): TlCodec<LiteServerRunMethodResult> = LiteServerRunMethodResult

    public companion object : TlCodec<LiteServerRunSmcMethod> by LiteServerRunSmcMethodTlConstructor {
        @JvmStatic
        public fun methodId(methodName: String): Long = crc16(methodName).toLong() or 0x10000

        @JvmStatic
        public fun createParams(
            vmStack: VmStack
        ): BagOfCells = BagOfCells(
            CellBuilder.createCell {
                storeTlb(VmStack, vmStack)
            }
        )

        @JvmStatic
        public fun createParams(
            params: Iterable<VmStackValue>
        ): BagOfCells = createParams(VmStack(VmStackList(params.asIterable())))

        @JvmStatic
        public fun createParams(
            vararg params: VmStackValue
        ): BagOfCells = createParams(params.asIterable())
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
        output.writeBoc(value.params)
    }

    override fun decode(input: TlReader): LiteServerRunSmcMethod {
        val mode = input.readInt()
        val id = input.read(TonNodeBlockIdExt)
        val account = input.read(LiteServerAccountId)
        val methodId = input.readLong()
        val params = input.readBoc()
        return LiteServerRunSmcMethod(mode, id, account, methodId, params)
    }
}
