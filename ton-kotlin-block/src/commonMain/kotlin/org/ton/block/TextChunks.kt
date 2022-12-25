package org.ton.block

import org.ton.tlb.TlbCodec

sealed interface TextChunks {
    companion object {
        fun tlbCodec(n: Int): TlbCodec<TextChunks> = if (n == 0) {
            TextChunkEmpty
        } else {
            TextChunk.tlbConstructor(n)
        } as TlbCodec<TextChunks>
    }
}
