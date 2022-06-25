package org.ton.boc

import io.ktor.utils.io.core.*
import org.ton.cell.Cell

fun BagOfCells(byteArray: ByteArray): BagOfCells = BagOfCells.of(byteArray)
fun BagOfCells(roots: Iterable<Cell>): BagOfCells = BagOfCells.of(roots)
fun BagOfCells(vararg roots: Cell): BagOfCells = BagOfCells.of(*roots)

interface BagOfCells : Iterable<Cell> {
    val roots: List<Cell>
    fun toByteArray(): ByteArray

    override fun toString(): String

    companion object {
        const val BOC_GENERIC_MAGIC = 0xB5EE9C72.toInt()
        const val BOC_INDEXED_MAGIC = 0x68FF65F3
        const val BOC_INDEXED_CRC32C_MAGIC = 0xACC3A728.toInt()

        @JvmStatic
        fun of(roots: Iterable<Cell>): BagOfCells {
            val rootsList = roots.toList()
            return CachedBagOfCells(rootsList)
        }

        @JvmStatic
        fun of(vararg roots: Cell): BagOfCells {
            val rootsList = roots.toList()
            return CachedBagOfCells(rootsList)
        }

        @JvmStatic
        fun of(byteArray: ByteArray): BagOfCells {
            return ByteReadPacket(byteArray).readBagOfCell()
        }
    }
}
