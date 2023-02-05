package org.ton.tonlib

import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.api.tonnode.TonNodeZeroStateIdExt
import org.ton.bitstring.Bits256
import org.ton.block.ShardIdent
import org.ton.tonlib.block.BlockIdExt

internal data class LastBlockState(
    val lastBlockId: BlockIdExt,
    val lastKeyBlockId: BlockIdExt,
    val utime: Long,
    val initBlockId: BlockIdExt,
    val zeroStateId: ZeroStateIdExt,
    val vertSeqno: Int
)

internal typealias WorkchainId = Int
internal typealias BlockSeqno = Int
internal typealias ShardId = Long
internal typealias RootHash = Bits256
internal typealias FileHash = Bits256
internal typealias BlockHash = Bits256
internal typealias NodeIdShort = Bits256
internal typealias CatchainSeqno = Int

internal const val MASTERCHAIN_ID: WorkchainId = -1
internal const val BASECHAIN_ID: WorkchainId = 0
internal const val WORKCHAIN_INVALID: WorkchainId = 0x80000000.toInt()
internal const val SHARD_ID_ALL: ShardId = 1L shl 63

internal data class ShardIdFull(
    val workchain: WorkchainId,
    val shard: ShardId,
) {
    constructor() : this(WORKCHAIN_INVALID, 0)
    constructor(workchain: WorkchainId) : this(workchain, SHARD_ID_ALL)
    constructor(tlb: ShardIdent) : this(tlb.workchainId, tlb.shardPrefix.toLong() or (1L shl (63 - tlb.shardPfxBits)))

    fun isValid(): Boolean = workchain != WORKCHAIN_INVALID
    fun isSplit(): Boolean = shard != SHARD_ID_ALL
    fun isMasterchain(): Boolean = workchain == MASTERCHAIN_ID

    fun toTlb(): ShardIdent = ShardIdent(shardPrefixLength(shard), workchain, (shard and (shard - 1)).toULong())

    override fun toString(): String = "($workchain,${shard.toULong().toString(16).padStart(16, '0')})"

    companion object {
        fun shardPrefixLength(shard: ShardId): Int =
            if (shard != 0L) 63 - shard.countLeadingZeroBits() else 0
    }
}

internal data class ZeroStateIdExt(
    val workchain: WorkchainId,
    val rootHash: RootHash,
    val fileHash: FileHash
) {
    constructor() : this(WORKCHAIN_INVALID, RootHash(), FileHash())
    constructor(zeroState: TonNodeBlockIdExt) : this(zeroState.workchain, zeroState.rootHash, zeroState.fileHash)
    constructor(zeroState: TonNodeZeroStateIdExt) : this(zeroState.workchain, zeroState.rootHash, zeroState.fileHash)

    fun isMasterchain(): Boolean = workchain == MASTERCHAIN_ID
    fun isValid(): Boolean = workchain != WORKCHAIN_INVALID

    override fun toString(): String = "$workchain:${rootHash.hex()}:${fileHash.hex()}"
}
