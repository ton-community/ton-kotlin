package org.ton.lite.api.liteserver

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.block.VmStack
import org.ton.boc.BagOfCells
import org.ton.lite.api.liteserver.internal.readBoc
import org.ton.lite.api.liteserver.internal.writeBoc
import org.ton.tl.*
import org.ton.tlb.CellRef
import kotlin.jvm.JvmStatic

@Serializable
public data class LiteServerRunMethodResult internal constructor(
    val mode: Int,
    val id: TonNodeBlockIdExt,
    @SerialName("shardblk")
    val shardBlock: TonNodeBlockIdExt,
    @SerialName("shard_proof")
    val shardProof: BagOfCells?,
    val proof: BagOfCells?,
    @SerialName("state_proof")
    val stateProof: BagOfCells?,
    @SerialName("init_c7")
    val initC7: BagOfCells?,
    @SerialName("lib_extras")
    val libExtras: BagOfCells?,
    @SerialName("exit_code")
    val exitCode: Int,
    val result: BagOfCells?
) {
    public constructor(
        id: TonNodeBlockIdExt,
        shardBlock: TonNodeBlockIdExt,
        shardProof: BagOfCells?,
        proof: BagOfCells?,
        stateProof: BagOfCells?,
        initC7: BagOfCells?,
        libExtras: BagOfCells?,
        exitCode: Int,
        result: BagOfCells?
    ) : this(
        mode(
            shardProof != null,
            stateProof != null,
            result != null,
            initC7 != null,
            libExtras != null,
        ),
        id,
        shardBlock,
        shardProof,
        proof,
        stateProof,
        initC7,
        libExtras,
        exitCode,
        result
    )

    public fun parseAsVmStack(): CellRef<VmStack>? = result?.let { CellRef(it.first(), VmStack) }

    public companion object LiteServerRunMethodResultTlConstructor : TlConstructor<LiteServerRunMethodResult>(
        schema = "liteServer.runMethodResult mode:# id:tonNode.blockIdExt shardblk:tonNode.blockIdExt shard_proof:mode.0?bytes proof:mode.0?bytes state_proof:mode.1?bytes init_c7:mode.3?bytes lib_extras:mode.4?bytes exit_code:int result:mode.2?bytes = liteServer.RunMethodResult"
    ) {
        @JvmStatic
        public fun mode(
            hasProof: Boolean = false,
            hasStateProof: Boolean = false,
            hasResult: Boolean = false,
            hasInitC7: Boolean = false,
            hasLibExtras: Boolean = false,
        ): Int {
            var mode = 0
            if (hasProof) mode = mode or 1
            if (hasStateProof) mode = mode or 2
            if (hasResult) mode = mode or 4
            if (hasInitC7) mode = mode or 8
            if (hasLibExtras) mode = mode or 16
            return mode
        }

        override fun encode(writer: TlWriter, value: LiteServerRunMethodResult) {
            writer.writeInt(value.mode)
            val mode = value.mode
            writer.write(TonNodeBlockIdExt, value.id)
            writer.write(TonNodeBlockIdExt, value.shardBlock)
            writer.writeNullable(mode, 0, value.shardProof) { writeBoc(it) }
            writer.writeNullable(mode, 0, value.proof) { writeBoc(it) }
            writer.writeNullable(mode, 1, value.stateProof) { writeBoc(it) }
            writer.writeNullable(mode, 3, value.initC7) { writeBoc(it) }
            writer.writeNullable(mode, 4, value.libExtras) { writeBoc(it) }
            writer.writeInt(value.exitCode)
            writer.writeNullable(mode, 2, value.result) { writeBoc(it) }
        }

        override fun decode(reader: TlReader): LiteServerRunMethodResult {
            val mode = reader.readInt()
            val id = reader.read(TonNodeBlockIdExt)
            val shardblk = reader.read(TonNodeBlockIdExt)
            val shardProof = reader.readNullable(mode, 0) { readBoc() }
            val proof = reader.readNullable(mode, 0) { readBoc() }
            val stateProof = reader.readNullable(mode, 1) { readBoc() }
            val initC7 = reader.readNullable(mode, 3) { readBoc() }
            val libExtras = reader.readNullable(mode, 4) { readBoc() }
            val exitCode = reader.readInt()
            val result = reader.readNullable(mode, 2) { readBoc() }
            return LiteServerRunMethodResult(
                mode,
                id,
                shardblk,
                shardProof,
                proof,
                stateProof,
                initC7,
                libExtras,
                exitCode,
                result
            )
        }
    }
}
