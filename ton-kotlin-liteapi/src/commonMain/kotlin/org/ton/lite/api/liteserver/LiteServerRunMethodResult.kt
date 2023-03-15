package org.ton.lite.api.liteserver

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.tl.*
import kotlin.jvm.JvmStatic

@Serializable
@SerialName("liteServer.runMethodResult")
public data class LiteServerRunMethodResult internal constructor(
    val mode: Int,
    val id: TonNodeBlockIdExt,
    @SerialName("shardblk")
    val shardBlock: TonNodeBlockIdExt,
    @SerialName("shard_proof")
    val shardProof: ByteArray?,
    val proof: ByteArray?,
    @SerialName("state_proof")
    val stateProof: ByteArray?,
    @SerialName("init_c7")
    val initC7: ByteArray?,
    @SerialName("lib_extras")
    val libExtras: ByteArray?,
    @SerialName("exit_code")
    val exitCode: Int,
    val result: ByteArray?
) {
    public constructor(
        id: TonNodeBlockIdExt,
        shardBlock: TonNodeBlockIdExt,
        shardProof: ByteArray?,
        proof: ByteArray?,
        stateProof: ByteArray?,
        initC7: ByteArray?,
        libExtras: ByteArray?,
        exitCode: Int,
        result: ByteArray?
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LiteServerRunMethodResult) return false

        if (mode != other.mode) return false
        if (id != other.id) return false
        if (shardBlock != other.shardBlock) return false
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
        result1 = 31 * result1 + shardBlock.hashCode()
        result1 = 31 * result1 + (shardProof?.contentHashCode() ?: 0)
        result1 = 31 * result1 + (proof?.contentHashCode() ?: 0)
        result1 = 31 * result1 + (stateProof?.contentHashCode() ?: 0)
        result1 = 31 * result1 + (initC7?.contentHashCode() ?: 0)
        result1 = 31 * result1 + (libExtras?.contentHashCode() ?: 0)
        result1 = 31 * result1 + exitCode
        result1 = 31 * result1 + (result?.contentHashCode() ?: 0)
        return result1
    }

    public companion object : TlCodec<LiteServerRunMethodResult> by LiteServerRunMethodResultTlConstructor {
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
    }
}

private object LiteServerRunMethodResultTlConstructor : TlConstructor<LiteServerRunMethodResult>(
    schema = "liteServer.runMethodResult mode:# id:tonNode.blockIdExt shardblk:tonNode.blockIdExt shard_proof:mode.0?bytes proof:mode.0?bytes state_proof:mode.1?bytes init_c7:mode.3?bytes lib_extras:mode.4?bytes exit_code:int result:mode.2?bytes = liteServer.RunMethodResult"
) {
    override fun encode(writer: TlWriter, value: LiteServerRunMethodResult) {
        writer.writeInt(value.mode)
        val mode = value.mode
        writer.write(TonNodeBlockIdExt, value.id)
        writer.write(TonNodeBlockIdExt, value.shardBlock)
        writer.writeNullable(mode, 0, value.shardProof) { writeBytes(it) }
        writer.writeNullable(mode, 0, value.proof) { writeBytes(it) }
        writer.writeNullable(mode, 1, value.stateProof) { writeBytes(it) }
        writer.writeNullable(mode, 3, value.initC7) { writeBytes(it) }
        writer.writeNullable(mode, 4, value.libExtras) { writeBytes(it) }
        writer.writeInt(value.exitCode)
        writer.writeNullable(mode, 2, value.result) { writeBytes(it) }
    }

    override fun decode(reader: TlReader): LiteServerRunMethodResult {
        val mode = reader.readInt()
        val id = reader.read(TonNodeBlockIdExt)
        val shardblk = reader.read(TonNodeBlockIdExt)
        val shardProof = reader.readNullable(mode, 0) { readBytes() }
        val proof = reader.readNullable(mode, 0) { readBytes() }
        val stateProof = reader.readNullable(mode, 1) { readBytes() }
        val initC7 = reader.readNullable(mode, 3) { readBytes() }
        val libExtras = reader.readNullable(mode, 4) { readBytes() }
        val exitCode = reader.readInt()
        val result = reader.readNullable(mode, 2) { readBytes() }
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
