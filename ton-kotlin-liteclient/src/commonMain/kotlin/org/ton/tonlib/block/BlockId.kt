package org.ton.tonlib.block

import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.tonlib.*

internal interface BlockId {
    val workchain: WorkchainId
    val shard: ShardId
    val seqno: BlockSeqno

    fun isValid(): Boolean = workchain != WORKCHAIN_INVALID
    fun isMasterchain(): Boolean = workchain == MASTERCHAIN_ID
    fun isMasterchainExt(): Boolean = isMasterchain() && shard == SHARD_ID_ALL

    companion object {
        fun of(
            shard: ShardIdFull,
            seqno: BlockSeqno
        ): BlockId = of(shard.workchain, shard.shard, seqno)

        fun of(
            workchain: WorkchainId = WORKCHAIN_INVALID,
            shard: ShardId = 0,
            seqno: BlockSeqno = 0
        ): BlockId = BlockIdImpl(workchain, shard, seqno)
    }
}

internal inline fun BlockId(workchain: WorkchainId, shard: ShardId, seqno: BlockSeqno): BlockId =
    BlockId.of(workchain, shard, seqno)

internal inline fun BlockId(shard: ShardIdFull, seqno: BlockSeqno): BlockId =
    BlockId.of(shard, seqno)

internal interface BlockIdExt : BlockId {
    val rootHash: RootHash
    val fileHash: FileHash
    val id: BlockId

    fun toTl(): TonNodeBlockIdExt = TonNodeBlockIdExt(workchain, shard, seqno, rootHash, fileHash)

    companion object {
        fun fromTl(
            tl: TonNodeBlockIdExt
        ): BlockIdExt = of(tl.workchain, tl.shard, tl.seqno, tl.rootHash, tl.fileHash)

        fun of(
            workchain: WorkchainId,
            shard: ShardId,
            seqno: BlockSeqno,
            rootHash: RootHash,
            fileHash: FileHash
        ): BlockIdExt = of(BlockId(workchain, shard, seqno), rootHash, fileHash)

        fun of(
            id: BlockId,
            rootHash: RootHash,
            fileHash: FileHash
        ): BlockIdExt = BlockIdExtImpl(id, rootHash, fileHash)
    }
}

internal inline fun BlockIdExt(
    tl: TonNodeBlockIdExt
): BlockIdExt = BlockIdExt.fromTl(tl)

internal inline fun BlockIdExt(
    workchain: WorkchainId,
    shard: ShardId,
    seqno: BlockSeqno,
    rootHash: RootHash,
    fileHash: FileHash
): BlockIdExt = BlockIdExt.of(workchain, shard, seqno, rootHash, fileHash)

internal inline fun BlockIdExt(
    id: BlockId,
    rootHash: RootHash,
    fileHash: FileHash
): BlockIdExt = BlockIdExt.of(id, rootHash, fileHash)

private class BlockIdImpl(
    override val workchain: WorkchainId,
    override val shard: ShardId,
    override val seqno: BlockSeqno,
) : BlockId {
    constructor(shardIdFull: ShardIdFull, seqno: BlockSeqno) : this(shardIdFull.workchain, shardIdFull.shard, seqno)
    constructor() : this(WORKCHAIN_INVALID, 0, 0)

    fun invalidate(): BlockId = BlockIdImpl(WORKCHAIN_INVALID, shard, seqno)

    override fun toString(): String = "($workchain:${shard.toULong().toString(16).padStart(16, '0')}:${seqno.toUInt()})"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BlockId) return false
        if (workchain != other.workchain) return false
        if (shard != other.shard) return false
        if (seqno != other.seqno) return false
        return true
    }

    override fun hashCode(): Int {
        var result = workchain
        result = 31 * result + shard.hashCode()
        result = 31 * result + seqno
        return result
    }
}

private class BlockIdExtImpl(
    override val id: BlockId,
    override val rootHash: RootHash = RootHash(),
    override val fileHash: FileHash = FileHash()
) : BlockIdExt, BlockId by id {
    override fun toString(): String = "$id:${rootHash.hex()}:${fileHash.hex()}"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BlockIdExt) return false
        if (id != other.id) return false
        if (rootHash != other.rootHash) return false
        if (fileHash != other.fileHash) return false
        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + rootHash.hashCode()
        result = 31 * result + fileHash.hashCode()
        return result
    }
}
