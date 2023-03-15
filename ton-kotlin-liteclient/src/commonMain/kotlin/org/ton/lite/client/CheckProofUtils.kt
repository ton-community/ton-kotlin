package org.ton.lite.client

import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.bitstring.Bits256
import org.ton.block.*
import org.ton.boc.BagOfCells
import org.ton.cell.Cell

internal object CheckProofUtils {
    fun checkBlockHeaderProof(
        root: Cell,
        blockId: TonNodeBlockIdExt,
        storeStateHash: Boolean = false,
    ): BlockHeaderResult {
        val virtualHash = root.hash()
        check(virtualHash == blockId.rootHash) {
            "Invalid hash for block: $blockId, expected: ${blockId.rootHash}, actual: $virtualHash"
        }
        val block = Block.loadTlb(root)
        val time = block.info.value.genUtime.toInt()
        val lt = block.info.value.endLt.toLong()
        var stateHash: Bits256? = null

        if (storeStateHash) {
            val stateUpdateCell = block.stateUpdate.toCell()
            stateHash = stateUpdateCell.refs[1].hash(0)
        }

        return BlockHeaderResult(time, lt, stateHash)
    }

    fun checkAccountProof(
        proof: ByteArray,
        shardBlock: TonNodeBlockIdExt,
        address: AddrStd,
        root: Cell
    ): AccountState? {
        val accountInfo = Account.loadTlb(root) as? AccountInfo ?: return null

        val qRoots = BagOfCells(proof).roots.toList()
        check(qRoots.size == 2) {
            "Invalid roots amount, expected: 2, actual: ${qRoots.size}"
        }

        val blockProofResult = checkBlockHeaderProof(MerkleProof.virtualize(qRoots[0]), shardBlock, true)

        val stateRoot = MerkleProof.virtualize(qRoots[1])
        val stateHash = stateRoot.hash()
        check(stateHash == blockProofResult.stateHash) {
            "Invalid state hash, expected: $stateHash, actual: ${blockProofResult.stateHash}"
        }

        val shardState = ShardState.loadTlb(stateRoot) as ShardStateUnsplit
        val shardAccount = checkNotNull(shardState.accounts.value.x[address.address]?.value) {
            "Shard account ${address.address} not found in shard state"
        }
        check(shardAccount.account.hash() == root.hash()) {
            "Account state hash mismatch, expected: ${shardAccount.account.hash()}, actual: ${root.hash()}"
        }

        return AccountState(accountInfo, shardAccount.lastTransHash, shardAccount.lastTransLt.toLong())
    }
}
