@file:Suppress("NOTHING_TO_INLINE")

package org.ton.api.tonnode

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readIntTl
import org.ton.tl.constructors.writeIntTl

inline fun TonNodeBlockId(tonNodeBlockId: TonNodeBlockId) = TonNodeBlockId.of(tonNodeBlockId)
inline fun TonNodeBlockId() = TonNodeBlockId.of()
inline fun TonNodeBlockId(workchain: Int, shard: Long, seqno: Int) = TonNodeBlockId.of(workchain, shard, seqno)
inline fun TonNodeBlockId(string: String) = TonNodeBlockId.parse(string)

interface TonNodeBlockId {
    val workchain: Int
    val shard: Long
    val seqno: Int

    fun isMasterchain() = workchain == Workchain.MASTERCHAIN_ID

    fun isValid(): Boolean = workchain != Workchain.INVALID_WORKCHAIN
    fun isValidExt(): Boolean = workchain.toLong() and shard != 0L
    fun isValidFull(): Boolean =
        isValid() && shard != 0L && shard and 7 <= 0 && (!isMasterchain() || shard == Shard.ID_ALL)

    fun withSeqno(seqno: Int): TonNodeBlockId = TonNodeBlockIdImpl(workchain, shard, seqno)

    operator fun component1() = workchain
    operator fun component2() = shard
    operator fun component3() = seqno

    companion object : TlCodec<TonNodeBlockId> by TonNodeBlockIdTlbConstructor {
        @JvmStatic
        fun of(): TonNodeBlockId = TonNodeBlockIdImpl(Workchain.INVALID_WORKCHAIN, 0, 0)

        @JvmStatic
        fun of(blockId: TonNodeBlockId): TonNodeBlockId =
            TonNodeBlockId(blockId.workchain, blockId.shard, blockId.seqno)

        @JvmStatic
        fun of(workchain: Int, shard: Long, seqno: Int): TonNodeBlockId = TonNodeBlockIdImpl(workchain, shard, seqno)

        @JvmStatic
        fun parse(string: String): TonNodeBlockId {
            require(string.getOrNull(0) == '(')
            require(string.getOrNull(string.lastIndex) == ')')
            val (rawWorkchain, rawShard, rawSeqno) = string.substring(1, string.lastIndex - 1).split(':')
            return TonNodeBlockIdImpl(rawWorkchain.toInt(), rawShard.toLong(), rawSeqno.toInt())
        }

        @JvmStatic
        fun parseOrNull(string: String): TonNodeBlockId? = try {
            parse(string)
        } catch (e: Exception) {
            null
        }
    }
}

@Serializable
private data class TonNodeBlockIdImpl(
    override val workchain: Int,
    override val shard: Long,
    override val seqno: Int
) : TonNodeBlockId {
    override fun withSeqno(seqno: Int): TonNodeBlockId = copy(seqno = seqno)

    override fun toString(): String = "($workchain:${shard.toString(16).uppercase()}:$seqno)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TonNodeBlockId) return false
        if (workchain != other.workchain) return false
        if (shard != other.shard) return false
        if (seqno != other.seqno) return false
        return true
    }
}

private object TonNodeBlockIdTlbConstructor : TlConstructor<TonNodeBlockId>(
    schema = "tonNode.blockId workchain:int shard:long seqno:int = tonNode.BlockId"
) {
    override fun decode(input: Input): TonNodeBlockId {
        val workchain = input.readIntTl()
        val shard = input.readLongLittleEndian()
        val seqno = input.readIntTl()
        return TonNodeBlockIdImpl(workchain, shard, seqno)
    }

    override fun encode(output: Output, value: TonNodeBlockId) {
        output.writeIntTl(value.workchain)
        output.writeLongLittleEndian(value.shard)
        output.writeIntTl(value.seqno)
    }
}
