package org.ton.block

import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.*
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

public data class StorageUsedShort(
    val cells: Long,
    val bits: Long
) : TlbObject {
    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter {
        return printer {
            type("storage_used_short") {
                field("cells", cells)
                field("bits", bits)
            }
        }
    }

    override fun toString(): String = print().toString()

    public companion object : TlbConstructorProvider<StorageUsedShort> by StorageUsedShortTlbConstructor {
        public val ZERO: StorageUsedShort = StorageUsedShort(0, 0)
    }
}

private object StorageUsedShortTlbConstructor : TlbConstructor<StorageUsedShort>(
    schema = "storage_used_short\$_ cells:(VarUInteger 7) bits:(VarUInteger 7) = StorageUsedShort;"
) {
    private val varUInteger7Codec = VarUInteger.tlbCodec(7)

    @Suppress("DEPRECATION")
    override fun storeTlb(
        cellBuilder: CellBuilder, value: StorageUsedShort
    ) = cellBuilder {
        storeTlb(varUInteger7Codec, VarUInteger(value.cells))
        storeTlb(varUInteger7Codec, VarUInteger(value.bits))
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): StorageUsedShort = cellSlice {
        val cells = loadTlb(varUInteger7Codec).value.toLong()
        val bits = loadTlb(varUInteger7Codec).value.toLong()
        StorageUsedShort(cells, bits)
    }
}
