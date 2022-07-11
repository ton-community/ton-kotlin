package org.ton.smartcontract

import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.hashmap.HashMapE
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor
import org.ton.tlb.constructor.tlbCodec
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

sealed interface FullContent {
    data class OnChain(
        val data: HashMapE<ContentData>
    ) : FullContent

    data class OffChain(
        val uri: Text
    ) : FullContent
}

private class FullContentTlbCombinator : TlbCombinator<FullContent>() {
    private val onChainConstructor by lazy { FUllContentOnChainConstructor() }
    private val offChainConstructor by lazy { FullContentOffChainConstructor() }

    override val constructors: List<TlbConstructor<out FullContent>> by lazy {
        listOf(onChainConstructor, offChainConstructor)
    }

    override fun getConstructor(value: FullContent): TlbConstructor<out FullContent> = when(value) {
        is FullContent.OnChain -> onChainConstructor
        is FullContent.OffChain -> offChainConstructor
    }

    private class FUllContentOnChainConstructor : TlbConstructor<FullContent.OnChain>(
        schema = "onchain#00 data:(HashMapE 256 ^ContentData) = FullContent;"
    ) {
        private val contentDataCodec by lazy { Cell.tlbCodec(ContentData.tlbCodec()) }
        private val hashMapECodec by lazy { HashMapE.tlbCodec(256, contentDataCodec) }

        override fun storeTlb(cellBuilder: CellBuilder, value: FullContent.OnChain) {
            cellBuilder.storeTlb(hashMapECodec, value.data)
        }

        override fun loadTlb(cellSlice: CellSlice): FullContent.OnChain = FullContent.OnChain(cellSlice.loadTlb(hashMapECodec))
    }


    private class FullContentOffChainConstructor : TlbConstructor<FullContent.OffChain>(
        schema = "offchain#01 uri:Text = FullContent;"
    ) {
        private val textCodec by lazy { Text.tlbCodec() }

        override fun storeTlb(cellBuilder: CellBuilder, value: FullContent.OffChain) {
            cellBuilder.storeTlb(textCodec, value.uri)
        }

        override fun loadTlb(cellSlice: CellSlice): FullContent.OffChain =
            FullContent.OffChain(cellSlice.loadTlb(textCodec))
    }
}
