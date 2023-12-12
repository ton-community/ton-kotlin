package org.ton.block

import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

public object ChunkRefEmpty : TextChunkRef, TlbConstructorProvider<ChunkRefEmpty> by ChunkRefEmptyTlbConstructor

private object ChunkRefEmptyTlbConstructor : TlbConstructor<ChunkRefEmpty>(
    schema = "chunk_ref_empty\$_ = TextChunkRef 0;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: ChunkRefEmpty) {
    }

    override fun loadTlb(cellSlice: CellSlice): ChunkRefEmpty = ChunkRefEmpty
}
