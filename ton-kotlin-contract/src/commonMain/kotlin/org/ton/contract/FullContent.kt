package org.ton.contract

import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.hashmap.HashMapE
import org.ton.tlb.*
import org.ton.tlb.constructor.tlbCodec

sealed interface FullContent {
    data class OnChain(
        val data: HashMapE<ContentData>
    ) : FullContent

    data class OffChain(
        val uri: Text
    ) : FullContent

    companion object : TlbCodec<FullContent> by FullContentCombinator {
        @JvmStatic
        fun tlbCombinator(): TlbCombinator<FullContent> = FullContentCombinator
    }
}

private object FullContentCombinator : TlbCombinator<FullContent>() {
    override val constructors: List<TlbConstructor<out FullContent>> =
        listOf(FullContentOnChainConstructor, FullContentOffChainConstructor)

    override fun getConstructor(value: FullContent): TlbConstructor<out FullContent> = when (value) {
        is FullContent.OnChain -> FullContentOnChainConstructor
        is FullContent.OffChain -> FullContentOffChainConstructor
    }

    private object FullContentOnChainConstructor : TlbConstructor<FullContent.OnChain>(
        schema = "onchain#00 data:(HashMapE 256 ^ContentData) = FullContent;"
    ) {
        val dataCodec = HashMapE.tlbCodec(256, Cell.tlbCodec(ContentData))

        override fun storeTlb(cellBuilder: CellBuilder, value: FullContent.OnChain) {
            cellBuilder.storeTlb(dataCodec, value.data)
        }

        override fun loadTlb(cellSlice: CellSlice): FullContent.OnChain =
            FullContent.OnChain(cellSlice.loadTlb(dataCodec))
    }


    private object FullContentOffChainConstructor : TlbConstructor<FullContent.OffChain>(
        schema = "offchain#01 uri:Text = FullContent;"
    ) {
        override fun storeTlb(cellBuilder: CellBuilder, value: FullContent.OffChain) {
            cellBuilder.storeTlb(Text, value.uri)
        }

        override fun loadTlb(cellSlice: CellSlice): FullContent.OffChain =
            FullContent.OffChain(cellSlice.loadTlb(Text))
    }
}
