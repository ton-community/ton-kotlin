@file:Suppress("NOTHING_TO_INLINE")

package org.ton.api.tonnode

import org.ton.cell.Cell
import org.ton.crypto.bitsNegative64
import org.ton.crypto.lowerBits64
import org.ton.crypto.toBoolean

object Shard {
    const val ID_ALL = 1L shl 63

    fun contains(parent: ULong, child: ULong): Boolean {
        val x = lowerBits64(parent)
        return !((parent xor child) and (bitsNegative64(x) shl 1)).toBoolean()
    }

    fun shardChild(shard: ULong, left: Boolean): ULong {
        val x = lowerBits64(shard) shr 1
        return if (left) shard - x else shard + x
    }

    fun shardParent(shard: ULong): ULong {
        val x = lowerBits64(shard)
        return (shard - x) or (x shl 1)
    }

    fun check(block: TonNodeBlockIdExt, shardBlock: TonNodeBlockIdExt, shardProof: Cell) {
        require(block.isMasterchain() && block.isValidFull()) { "block must belong to the masterchain" }
    }
}
