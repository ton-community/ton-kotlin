@file:Suppress("NOTHING_TO_INLINE")

package org.ton.api.tonnode

import org.ton.cell.Cell

public object Shard {
    public const val ID_ALL: Long = 1L shl 63

    public fun contains(parent: Long, child: Long): Boolean {
        val x = lowerBits64(parent)
        return ((parent xor child) and (bitsNegative64(x) shl 1)) == 0L
    }

    public fun shardChild(shard: Long, left: Boolean): Long {
        val x = lowerBits64(shard) ushr 1
        return if (left) shard - x else shard + x
    }

    public fun shardParent(shard: Long): Long {
        val x = lowerBits64(shard)
        return (shard - x) or (x shl 1)
    }

    public fun check(block: TonNodeBlockIdExt, shardBlock: TonNodeBlockIdExt, shardProof: Cell) {
        require(block.isMasterchain() && block.isValidFull()) { "block must belong to the masterchain" }
    }

    internal inline fun lowerBits64(x: Long) = x and bitsNegative64(x)
    internal inline fun bitsNegative64(x: Long) = x.inv() + 1
}
