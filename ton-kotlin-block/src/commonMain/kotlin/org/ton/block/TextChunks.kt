package org.ton.block

import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor

sealed interface TextChunks {
    companion object {
        fun tlbCodec(n: Int): TlbCodec<TextChunks> = if (n == 0) {
            TextChunkEmpty
        } else {
            TextChunk.tlbConstructor(n)
        } as TlbCodec<TextChunks>
    }
}
