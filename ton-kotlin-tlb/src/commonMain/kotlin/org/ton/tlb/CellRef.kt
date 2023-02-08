package org.ton.tlb

import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.CellType
import kotlin.jvm.JvmStatic

public inline fun <T> CellRef(cell: Cell, codec: TlbCodec<T>): CellRef<T> = CellRef.valueOf(cell, codec)
public inline fun <T> CellRef(value: T): CellRef<T> = CellRef.valueOf(value)
public inline fun <T> CellRef(codec: TlbCodec<T>): TlbCodec<CellRef<T>> = CellRef.tlbCodec(codec)

public inline fun <T> Cell.asRef(codec: TlbCodec<T>): CellRef<T> = CellRef.valueOf(this, codec)

public interface CellRef<out T> : TlbObject {
    public val value: T

    public fun toCell(codec: TlbCodec<@UnsafeVariance T>? = null): Cell

    public operator fun getValue(thisRef: Any?, property: Any?): T = value

    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter {
        val value = value
        return if (value is TlbObject) {
            value.print(printer)
        } else {
            printer {
                type(value.toString())
            }
        }
    }

    public companion object {
        @JvmStatic
        public fun <T> valueOf(cell: Cell, codec: TlbCodec<T>): CellRef<T> = CellRefImpl(cell, codec)

        @JvmStatic
        public fun <T> valueOf(value: T): CellRef<T> = CellRefValue(value)

        @JvmStatic
        public fun <T> tlbCodec(codec: TlbCodec<T>): TlbCodec<CellRef<T>> = CellRefTlbConstructor(codec)
    }
}

private class CellRefImpl<T>(
    val cell: Cell,
    val codec: TlbCodec<T>
) : CellRef<T> {
    override val value: T by lazy(LazyThreadSafetyMode.PUBLICATION) {
        check(cell.type == CellType.ORDINARY) { "Can't load reference value: $cell" }
        codec.loadTlb(cell)
    }

    override fun toCell(codec: TlbCodec<T>?): Cell {
        return cell
    }

    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter {
        return if (cell.type == CellType.PRUNED_BRANCH) {
            printer.type("!pruned_branch") {
                field("cell", cell.bits)
            }
        } else {
            super.print(printer)
        }
    }

    override fun toString(): String = "CellRef($cell)"
}

private class CellRefValue<T>(
    override val value: T,
) : CellRef<T> {
    override fun toCell(codec: TlbCodec<T>?): Cell {
        require(codec != null) { "Codec is not specified" }
        return CellBuilder.createCell {
            codec.storeTlb(this, value)
        }
    }

    override fun toString(): String = "CellRef($value)"
}

private class CellRefTlbConstructor<T>(
    val codec: TlbCodec<T>
) : TlbCodec<CellRef<T>> {
    override fun storeTlb(cellBuilder: CellBuilder, value: CellRef<T>) {
        cellBuilder.storeRef(value.toCell(codec))
    }

    override fun loadTlb(cellSlice: CellSlice): CellRef<T> {
        return cellSlice.loadRef().asRef(codec)
    }
}

public inline fun <T> CellBuilder.storeRef(codec: TlbCodec<T>, value: CellRef<T>) {
    storeRef(value.toCell(codec))
}

public inline fun <T> CellSlice.loadRef(codec: TlbCodec<T>): CellRef<T> {
    return loadRef().asRef(codec)
}
