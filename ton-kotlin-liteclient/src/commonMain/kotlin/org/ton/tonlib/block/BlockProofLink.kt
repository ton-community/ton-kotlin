package org.ton.tonlib.block

import org.ton.bitstring.Bits256
import org.ton.bitstring.toBitString
import org.ton.block.Block
import org.ton.block.BlockInfo
import org.ton.block.ShardStateUnsplit
import org.ton.cell.Cell
import org.ton.tonlib.CatchainSeqno
import org.ton.tonlib.ShardIdFull

internal data class BlockProofLink(
    val from: BlockIdExt,
    val to: BlockIdExt,
    val isKey: Boolean = false,
    val isForward: Boolean = to.seqno > from.seqno,
    val destProof: Cell? = null,
    val stateProof: Cell? = null,
    val proof: Cell? = null,
    val ccSeqno: CatchainSeqno = 0,
    val validatorSetHash: Int = 0,
    val signatures: List<BlockSignature> = emptyList()
) {
    fun isIncomplete(): Boolean = destProof == null
    fun validate() {
        check(from.isMasterchainExt() && to.isMasterchainExt()) {
            "BlockProofLink must have both source and destination blocks in the masterchain"
        }
        check(from.seqno != to.seqno) {
            "BlockProofLink connects two masterchain blocks $from and $to of equal height"
        }
        check(isForward == (from.seqno < to.seqno)) {
            "BlockProofLink from $from to $to is incorrectly declared as a ${if (isForward) "forward" else "backward"} link"
        }
        check(destProof != null || to.seqno == 0) {
            "BlockProofLink contains no proof for destination block $to"
        }
        checkNotNull(proof) {
            "BlockProofLink contains no proof for source block $from"
        }
        check(isForward || stateProof != null) {
            "a backward BlockProofLink contains no proof for the source state of $from"
        }
        check(!isForward || signatures.isNotEmpty()) {
            "a forward BlockProofLink from $from to $to contains no signatures"
        }
        val vsRoot = proof.virtualize()
        val stateHash = if (from.seqno != 0) checkBlockHeader(vsRoot, from, !isForward) else null
        val vdRoot = destProof?.virtualize()
        if (to.seqno != 0) {
            checkNotNull(vdRoot) {
                "BlockProofLink contains no proof for destination block $to"
            }
            checkBlockHeader(vdRoot, to)
            val block: Block
            val info: BlockInfo
            try {
                block = Block.loadTlb(vdRoot)
                info = block.info.value
            } catch (t: Throwable) {
                throw IllegalStateException("Can't deserialize header for block $to", t)
            }
            check(info.keyBlock == isKey) {
                "Invalid is_key_block value $isKey for destination block $to"
            }
        }
        if (!isForward) {
            val vStateRoot = requireNotNull(stateProof?.virtualize()) {
                "Backward BlockProofLink contains no proof for the source state of $from"
            }
            val actualStateHash = Bits256(vStateRoot.hash())
            check(stateHash == actualStateHash) {
                "BlockProofLink hash mismatch for $from, expected: $stateHash, actual: $actualStateHash"
            }

            val rootInfo = ShardStateUnsplit.loadTlb(vStateRoot)
            val prevBlocksDict = rootInfo.custom.value?.value?.r1?.value?.prevBlocks
        }
    }
}

private fun checkBlockHeader(
    blockRoot: Cell,
    id: BlockIdExt,
    computeShardHash: Boolean = false
): Bits256? {
    val blk: Block
    val info: BlockInfo
    val shard: ShardIdFull
    try {
        blk = Block.loadTlb(blockRoot)
        info = blk.info.value
        shard = ShardIdFull(info.shard)
    } catch (e: Throwable) {
        throw IllegalArgumentException("BlockProofLink contains invalid proof for block $id: \n$blockRoot", e)
    }
    val headerId = BlockId(shard, info.seqNo)
    check(id.id == headerId) {
        "Block header id mismatch, expected: $id"
    }
    val expectedRootHash = id.rootHash.value
    val actualRootHash = blockRoot.hash().toBitString()
    check(expectedRootHash == actualRootHash) {
        "Block header root hash mismatch, expected: $expectedRootHash, actual: $actualRootHash"
    }
    check(info.notMaster == !shard.isMasterchain()) {
        "Block has invalid not_master flag in its (Merkle) header"
    }
    return if (computeShardHash) Bits256(blk.stateUpdate.value.new.toCell().hash(0)) else null
}
