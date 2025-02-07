package org.ton.kotlin.cell

import org.ton.kotlin.cell.serialization.CellSerializer

/**
 * Lazy-loaded model.
 */
public data class CellRef<T>(
    public val cell: Cell,
    private val serializer: CellSerializer<T>
) : CellSerializer<T> {
    public constructor(value: T, serializer: CellSerializer<T>, context: CellContext = CellContext.EMPTY) : this(
        context.finalizeCell(CellBuilder().store(serializer, value, context)),
        serializer
    )

    public fun load(context: CellContext = CellContext.EMPTY): T {
        return load(context.loadCell(cell).asCellSlice(), context)
    }

    override fun load(slice: CellSlice, context: CellContext): T {
        return slice.load(serializer, context)
    }

    override fun store(builder: CellBuilder, value: T, context: CellContext) {
        builder.store(serializer, value, context)
    }
}