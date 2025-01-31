package org.ton.cell.serialization

import org.ton.cell.CellBuilder
import org.ton.cell.CellContext
import org.ton.cell.CellSlice

public interface CellParser<out T> {
    public fun parse(slice: CellSlice, context: CellContext = CellContext.EMPTY)
}

public interface CellStorer<in T> {
    public fun store(builder: CellBuilder, value: T, context: CellContext = CellContext.EMPTY)
}

public interface CellSerializer<T> : CellStorer<T>, CellParser<T>