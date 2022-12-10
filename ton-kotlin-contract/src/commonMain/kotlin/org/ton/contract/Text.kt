package org.ton.contract

import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.providers.TlbCombinatorProvider
import org.ton.tlb.providers.TlbConstructorProvider
import org.ton.tlb.storeTlb

data class Text(
    val data: SnakeData
) {
    companion object : TlbConstructorProvider<Text> by TextConstructor
}

private object TextConstructor : TlbConstructor<Text>(
    schema = "text#_ {n:#} data:(SnakeData ~n) = Text;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: Text) {
        cellBuilder.storeTlb(SnakeData, value.data)
    }

    override fun loadTlb(cellSlice: CellSlice): Text = Text(cellSlice.loadTlb(SnakeData))
}
