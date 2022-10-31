package org.ton.block

import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor

sealed interface TextChunkRef {
    companion object {
        fun tlbCombinator(n: Int): TlbCombinator<TextChunkRef> = TextChunkRefTlbCombinator(n)
    }
}

private class TextChunkRefTlbCombinator(
    n: Int
) : TlbCombinator<TextChunkRef>() {
    private val constructor = if (n == 0) {
        ChunkRefEmpty.tlbConstructor()
    } else {
        ChunkRef.tlbConstructor(n)
    }

    override val constructors: List<TlbConstructor<out TextChunkRef>>
        get() = listOf(constructor)
}
