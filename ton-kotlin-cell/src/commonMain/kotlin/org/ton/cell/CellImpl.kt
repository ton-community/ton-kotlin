package org.ton.cell

import org.ton.bitstring.BitString
import org.ton.cell.Cell.Companion.DEPTH_BYTES
import org.ton.cell.Cell.Companion.HASH_BYTES
import org.ton.cell.Cell.Companion.getBitsDescriptor
import org.ton.cell.Cell.Companion.getRefsDescriptor
import org.ton.crypto.digest.sha256
import org.ton.crypto.encodeHex
import kotlin.jvm.JvmStatic
import kotlin.math.max

internal class CellImpl(
    override val bits: BitString = BitString(0),
    override val refs: List<Cell> = emptyList(),
    override val type: CellType = CellType.ORDINARY,
    override val levelMask: LevelMask,
    private val hashes: Array<ByteArray> = emptyArray(),
    private val depths: Array<Int> = emptyArray(),
) : Cell {
    private val hashCode = hash().contentHashCode()

    override fun hash(level: Int): ByteArray {
        var hashIndex = levelMask.apply(level).hashIndex
        if (type == CellType.PRUNED_BRANCH) {
            val thisHashIndex = levelMask.hashIndex
            if (hashIndex != thisHashIndex) {
                val offset = (2 + hashIndex * HASH_BYTES)
                return bits.toByteArray().copyOfRange(offset, offset + HASH_BYTES)
            }
            hashIndex = 0
        }
        return hashes[hashIndex]
    }

    override fun depth(level: Int): Int {
        var hashIndex = levelMask.apply(level).hashIndex
        if (type == CellType.PRUNED_BRANCH) {
            val thisHashIndex = levelMask.hashIndex
            if (hashIndex != thisHashIndex) {
                val bytes = bits.toByteArray()
                val offset = 2 + thisHashIndex * HASH_BYTES + hashIndex * DEPTH_BYTES
                return (bytes[offset].toInt() shl 8) or bytes[offset + 1].toInt()
            }
            hashIndex = 0
        }
        return depths[hashIndex]
    }

    override fun virtualize(offset: Int): Cell {
        return if (levelMask.isEmpty()) {
            this
        } else {
            VirtualCell(this, offset)
        }
    }

    override fun toString(): String = Cell.toString(this)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Cell) return false
        if (!hash().contentEquals(other.hash())) return false
        return true
    }

    override fun hashCode(): Int = hashCode

    companion object {
        @JvmStatic
        fun of(
            bits: BitString,
            refs: List<Cell> = emptyList(),
            isExotic: Boolean = false
        ): Cell {
            val dataBytes = bits.toByteArray()
            val type = if (isExotic) {
                require(dataBytes.size > 1) { "Not enough data for exotic cell" }
                CellType[dataBytes[0].toInt()]
            } else CellType.ORDINARY
            var levelMask = LevelMask()
            when (type) {
                CellType.ORDINARY -> {
                    refs.forEach { ref ->
                        levelMask = levelMask or ref.levelMask
                    }
                }

                CellType.PRUNED_BRANCH -> {
                    require(refs.isEmpty()) {
                        "Pruned branch cell has a cell reference"
                    }
                    require(dataBytes.size > 2) {
                        "Not enough data for a pruned branch cell"
                    }
                    levelMask = LevelMask(dataBytes[1].toInt())
                    val level = levelMask.level
                    require(level in 0..3) {
                        "Pruned branch cell has an invalid level"
                    }
                    val newLevelMask = levelMask.apply(level - 1)
                    val hashes = newLevelMask.hashCount
                    require(dataBytes.size == (2 + hashes * (HASH_BYTES + DEPTH_BYTES))) {
                        "Not enough data for pruned branch cell"
                    }
                }

                CellType.LIBRARY_REFERENCE -> {
                    require(dataBytes.size == 1 + HASH_BYTES) {
                        "Not enough data for library reference cell"
                    }
                }

                CellType.MERKLE_PROOF -> {
                    require(dataBytes.size == 1 + HASH_BYTES + DEPTH_BYTES) {
                        "Not enough data for merkle proof cell"
                    }
                    require(refs.size == 1) {
                        "Wrong references count for a merkle proof cell"
                    }
                    val merkleHash = dataBytes.copyOfRange(1, 1 + HASH_BYTES)
                    val childHash = refs[0].hash(level = 0)
                    check(merkleHash.contentEquals(childHash)) {
                        "Hash mismatch in merkle proof cell, merkle hash: ${merkleHash.encodeHex()} , child hash: ${childHash.encodeHex()}"
                    }
                    val merkleDepth = (dataBytes[1 + HASH_BYTES].toInt() shl 8) or
                        dataBytes[2 + HASH_BYTES].toInt()
                    val childDepth = refs[0].depth(level = 0)
                    check(merkleDepth == childDepth) {
                        "Depth mismatch in merkle proof cell, merkle depth: $merkleDepth , child depth: $childDepth"
                    }
                    levelMask = refs[0].levelMask shr 1
                }

                CellType.MERKLE_UPDATE -> {
                    require(dataBytes.size == 1 + (HASH_BYTES + DEPTH_BYTES) * 2) {
                        "Not enough data for merkle update cell"
                    }
                    require(refs.size == 2) {
                        "Wrong references count for merkle update cell"
                    }
                    val merkleHash0 = dataBytes.copyOfRange(1, 1 + HASH_BYTES)
                    val childHash0 = refs[0].hash(level = 0)
                    check(merkleHash0.contentEquals(childHash0)) {
                        "First hash mismatch in merkle update cell:\n" +
                            "merkle hash: ${merkleHash0.encodeHex()}\n" +
                            " child hash: ${childHash0.encodeHex()}\n" +
                            "       data: ${dataBytes.encodeHex()}"
                    }
                    val merkleHash1 = dataBytes.copyOfRange(1 + HASH_BYTES, 1 + HASH_BYTES * 2)
                    val childHash1 = refs[1].hash(level = 0)
                    check(merkleHash1.contentEquals(childHash1)) {
                        "Second hash mismatch in merkle update cell:\n" +
                            "merkle hash: ${merkleHash1.encodeHex()}\n" +
                            " child hash: ${childHash1.encodeHex()}\n" +
                            "       data: ${dataBytes.encodeHex()}"
                    }
                    val depthOffset0 = 1 + 2 * HASH_BYTES
                    val merkleDepth0 = (dataBytes[depthOffset0].toInt() shl 8) or
                        dataBytes[depthOffset0 + 1].toInt()
                    val childDepth0 = refs[0].depth(level = 0)
                    check(merkleDepth0 == childDepth0) {
                        "First depth mismatch in merkle update cell:\n" +
                            "merkle depth: $merkleDepth0\n" +
                            " child depth: $childDepth0\n" +
                            "       data: ${dataBytes.encodeHex()}"
                    }
                    val depthOffset1 = 1 + 2 * HASH_BYTES + DEPTH_BYTES
                    val merkleDepth1 = (dataBytes[depthOffset1].toInt() shl 8) or
                        (dataBytes[depthOffset1 + 1].toInt())
                    val childDepth1 = refs[1].depth(level = 0)
                    check(merkleDepth1 == childDepth1) {
                        "Second depth mismatch in merkle update cell:\n" +
                            "merkle depth: $merkleDepth1\n" +
                            " child depth: $childDepth1\n" +
                            "       data: ${dataBytes.encodeHex()}"
                    }
                    levelMask = (refs[0].levelMask or refs[1].levelMask) shr 1
                }
            }
            val totalHashCount = levelMask.hashCount
            val hashCount = if (type == CellType.PRUNED_BRANCH) 1 else totalHashCount
            val hashIndexOffset = totalHashCount - hashCount

            val hashes = Array(hashCount) { byteArrayOf() }
            val depths = Array(hashCount) { 0 }

            val level = levelMask.level
            var hashIndex = 0
            repeat(level + 1) { levelIndex ->
                if (!levelMask.isSignificant(levelIndex)) return@repeat
                if (hashIndex < hashIndexOffset) {
                    hashIndex++
                    return@repeat
                }
                val newLevelMask = levelMask.apply(levelIndex)
                val d1 = getRefsDescriptor(refs.size, isExotic, newLevelMask)
                val d2 = getBitsDescriptor(bits)
                var representation = byteArrayOf(d1, d2)
                representation += if (hashIndex == hashIndexOffset) {
                    check(levelIndex == 0 || type == CellType.PRUNED_BRANCH)
                    bits.toByteArray(augment = true)
                } else {
                    hashes[hashIndex - hashIndexOffset - 1]
                }
                val destIndex = hashIndex - hashIndexOffset
                var depth = 0
                refs.forEach { ref ->
                    val childDepth = if (type.isMerkle) {
                        ref.depth(levelIndex + 1)
                    } else {
                        ref.depth(levelIndex)
                    }
                    representation += byteArrayOf(
                        (childDepth shr 8).toByte(),
                        childDepth.toByte()
                    )
                    depth = max(depth, childDepth)
                }
                if (refs.isNotEmpty()) {
                    check(depth < 1024) {
                        "Depth is too big"
                    }
                    depth++
                }
                depths[destIndex] = depth
                refs.forEach { ref ->
                    representation += if (type.isMerkle) {
                        ref.hash(levelIndex + 1)
                    } else {
                        ref.hash(levelIndex)
                    }
                }
                hashes[destIndex] = sha256(representation)
                hashIndex++
            }
            return CellImpl(
                bits, refs, type,
                levelMask, hashes, depths
            )
        }
    }
}
