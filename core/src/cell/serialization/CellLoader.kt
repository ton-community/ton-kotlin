package org.ton.kotlin.cell.serialization

import org.ton.kotlin.cell.CellBuilder
import org.ton.kotlin.cell.CellContext
import org.ton.kotlin.cell.CellSlice

public fun interface CellLoader<out T> {
    public fun load(slice: CellSlice): T = load(slice, CellContext.EMPTY)
    public fun load(slice: CellSlice, context: CellContext): T
}

public fun interface CellStorer<in T> {
    public fun store(builder: CellBuilder, value: T): Unit = store(builder, value, CellContext.EMPTY)
    public fun store(builder: CellBuilder, value: T, context: CellContext)
}

public interface CellSerializer<T> : CellLoader<T>, CellStorer<T>