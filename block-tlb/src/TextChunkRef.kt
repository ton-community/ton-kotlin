package org.ton.block

import org.ton.tlb.TlbCombinator

public sealed interface TextChunkRef {
    public companion object {
        public fun tlbCombinator(n: Int): TlbCombinator<TextChunkRef> = if (n == 0) {
            ChunkRefEmpty.tlbConstructor()
        } else {
            ChunkRef.tlbConstructor(n)
        } as TlbCombinator<TextChunkRef>
    }
}
