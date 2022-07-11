package org.ton.smartcontract

import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

data class Text(
    val data: SnakeData
) {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbCombinator<Text> = TextTlbCombinator()
    }
}

private class TextTlbCombinator : TlbCombinator<Text>() {
    private val textConstructor by lazy { TextConstructor() }

    override val constructors: List<TlbConstructor<out Text>> by lazy { listOf(textConstructor) }

    override fun getConstructor(value: Text): TlbConstructor<out Text> = textConstructor

    private class TextConstructor : TlbConstructor<Text>(
        schema = "text#_ {n:#} data:(SnakeData ~n) = Text;"
    ) {
        private val snakeDataCodec by lazy { SnakeData.tlbCodec() }

        override fun storeTlb(cellBuilder: CellBuilder, value: Text) {
            cellBuilder.storeTlb(snakeDataCodec, value.data)
        }

        override fun loadTlb(cellSlice: CellSlice): Text = Text(cellSlice.loadTlb(snakeDataCodec))
    }
}
