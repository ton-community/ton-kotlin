package org.ton.block

import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.loadRef
import org.ton.cell.storeRef
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

data class ChunkRef(
    val ref: TextChunks
) : TextChunkRef {
    companion object {
        fun tlbConstructor(n: Int): TlbConstructor<ChunkRef> = ChunkRefTlbConstructor(n)
    }
}

private class ChunkRefTlbConstructor(
    n: Int
) : TlbConstructor<ChunkRef>(
    schema = "chunk_ref\$_ {n:#} ref:^(TextChunks (n + 1)) = TextChunkRef (n + 1);\n"
) {
    val n = n - 1

    override fun storeTlb(cellBuilder: CellBuilder, value: ChunkRef) {
        cellBuilder.storeRef {
            storeTlb(TextChunks.tlbCombinator(n + 1), value.ref)
        }
    }

    override fun loadTlb(cellSlice: CellSlice): ChunkRef {
        val ref = cellSlice.loadRef {
            loadTlb(TextChunks.tlbCombinator(n + 1))
        }
        return ChunkRef(ref)
    }
}
