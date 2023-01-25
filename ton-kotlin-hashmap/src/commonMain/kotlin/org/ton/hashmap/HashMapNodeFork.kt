package org.ton.hashmap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.Cell
import org.ton.tlb.CellRef
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbPrettyPrinter

@Serializable
@SerialName("hmn_fork")
public data class HashMapNodeFork<T>(
    val leftCellRef: CellRef<HashMapEdge<T>>,
    val rightCellRef: CellRef<HashMapEdge<T>>
) : HashMapNode<T> {
    public constructor(
        left: Cell,
        right: Cell,
        codec: TlbCodec<HashMapEdge<T>>
    ) : this(
        leftCellRef = CellRef(left, codec),
        rightCellRef = CellRef(right, codec)
    )

    public constructor(
        left: HashMapEdge<T>,
        right: HashMapEdge<T>,
    ) : this(
        leftCellRef = CellRef(left),
        rightCellRef = CellRef(right)
    )

    val left: HashMapEdge<T> by leftCellRef
    val right: HashMapEdge<T> by rightCellRef

    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer.type("hmn_fork") {
        field("left", left)
        field("right", right)
    }

    override fun toString(): String = print().toString()
}
