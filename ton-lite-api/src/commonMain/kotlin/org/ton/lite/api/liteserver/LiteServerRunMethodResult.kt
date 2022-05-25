package org.ton.lite.api.liteserver

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.bitstring.toBits
import org.ton.block.VmStack
import org.ton.block.VmStackValue
import org.ton.block.tlb.tlbCodec
import org.ton.cell.BagOfCells
import org.ton.crypto.Base64ByteArraySerializer
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readBytesTl
import org.ton.tl.constructors.writeBytesTl
import org.ton.tl.readTl
import org.ton.tl.writeTl
import org.ton.tlb.loadTlb

@Serializable
data class LiteServerRunMethodResult(
    val mode: Int,
    val id: TonNodeBlockIdExt,
    val shardblk: TonNodeBlockIdExt,
    @SerialName("shard_proof")
    @Serializable(Base64ByteArraySerializer::class)
    val shardProof: ByteArray?,
    @Serializable(Base64ByteArraySerializer::class)
    val proof: ByteArray?,
    @SerialName("state_proof")
    @Serializable(Base64ByteArraySerializer::class)
    val stateProof: ByteArray?,
    @SerialName("init_c7")
    @Serializable(Base64ByteArraySerializer::class)
    val initC7: ByteArray?,
    @SerialName("lib_extras")
    @Serializable(Base64ByteArraySerializer::class)
    val libExtras: ByteArray?,
    @SerialName("exit_code")
    val exitCode: Int,
    @Serializable(Base64ByteArraySerializer::class)
    val result: ByteArray?
) : Iterable<VmStackValue> {

    fun resultBagOfCells(): BagOfCells? =
        result?.let { BagOfCells(it) }

    fun resultStack(): VmStack? =
        resultBagOfCells()?.roots?.first()?.parse {
            loadTlb(vmStackCodec)
        }

    fun resultValues(): List<VmStackValue>? =
        resultStack()?.stack?.reversed()

    operator fun get(index: Int) = resultValues()?.get(index)
    override operator fun iterator(): Iterator<VmStackValue> = resultValues().orEmpty().iterator()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LiteServerRunMethodResult

        if (mode != other.mode) return false
        if (id != other.id) return false
        if (shardblk != other.shardblk) return false
        if (shardProof != null) {
            if (other.shardProof == null) return false
            if (!shardProof.contentEquals(other.shardProof)) return false
        } else if (other.shardProof != null) return false
        if (proof != null) {
            if (other.proof == null) return false
            if (!proof.contentEquals(other.proof)) return false
        } else if (other.proof != null) return false
        if (stateProof != null) {
            if (other.stateProof == null) return false
            if (!stateProof.contentEquals(other.stateProof)) return false
        } else if (other.stateProof != null) return false
        if (initC7 != null) {
            if (other.initC7 == null) return false
            if (!initC7.contentEquals(other.initC7)) return false
        } else if (other.initC7 != null) return false
        if (libExtras != null) {
            if (other.libExtras == null) return false
            if (!libExtras.contentEquals(other.libExtras)) return false
        } else if (other.libExtras != null) return false
        if (exitCode != other.exitCode) return false
        if (result != null) {
            if (other.result == null) return false
            if (!result.contentEquals(other.result)) return false
        } else if (other.result != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result1 = mode
        result1 = 31 * result1 + id.hashCode()
        result1 = 31 * result1 + shardblk.hashCode()
        result1 = 31 * result1 + (shardProof?.contentHashCode() ?: 0)
        result1 = 31 * result1 + (proof?.contentHashCode() ?: 0)
        result1 = 31 * result1 + (stateProof?.contentHashCode() ?: 0)
        result1 = 31 * result1 + (initC7?.contentHashCode() ?: 0)
        result1 = 31 * result1 + (libExtras?.contentHashCode() ?: 0)
        result1 = 31 * result1 + exitCode
        result1 = 31 * result1 + (result?.contentHashCode() ?: 0)
        return result1
    }

    companion object LiteServerRunMethodResultTlConstructor : TlConstructor<LiteServerRunMethodResult>(
        type = LiteServerRunMethodResult::class,
        schema = "liteServer.runMethodResult mode:# id:tonNode.blockIdExt shardblk:tonNode.blockIdExt shard_proof:mode.0?bytes proof:mode.0?bytes state_proof:mode.1?bytes init_c7:mode.3?bytes lib_extras:mode.4?bytes exit_code:int result:mode.2?bytes = liteServer.RunMethodResult"
    ) {
        private val vmStackCodec by lazy { VmStack.tlbCodec() }

        override fun encode(output: Output, value: LiteServerRunMethodResult) {
            output.writeIntLittleEndian(value.mode)
            output.writeTl(TonNodeBlockIdExt, value.id)
            output.writeTl(TonNodeBlockIdExt, value.shardblk)
            value.shardProof?.let { shardProof ->
                output.writeBytesTl(shardProof)
            }
            value.shardProof?.let { proof ->
                output.writeBytesTl(proof)
            }
            value.stateProof?.let { stateProof ->
                output.writeBytesTl(stateProof)
            }
            value.initC7?.let { initC7 ->
                output.writeBytesTl(initC7)
            }
            value.libExtras?.let { libExtras ->
                output.writeBytesTl(libExtras)
            }
            output.writeIntLittleEndian(value.exitCode)
            value.result?.let { result ->
                output.writeBytesTl(result)
            }
        }

        override fun decode(input: Input): LiteServerRunMethodResult {
            val mode = input.readIntLittleEndian()
            val modeBits = mode.toBits(5)
            val id = input.readTl(TonNodeBlockIdExt)
            val shardblk = input.readTl(TonNodeBlockIdExt)
            val shardProof = if (modeBits[0]) input.readBytesTl() else null
            val proof = if (modeBits[0]) input.readBytesTl() else null
            val stateProof = if (modeBits[1]) input.readBytesTl() else null
            val initC7 = if (modeBits[3]) input.readBytesTl() else null
            val libExtras = if (modeBits[4]) input.readBytesTl() else null
            val exitCode = input.readIntLittleEndian()
            val result = if (modeBits[2]) input.readBytesTl() else null
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
