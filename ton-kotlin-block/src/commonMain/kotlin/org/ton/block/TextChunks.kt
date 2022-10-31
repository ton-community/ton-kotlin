package org.ton.block

import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor

sealed interface TextChunks {
    companion object {
        fun tlbCombinator(n: Int): TlbCombinator<TextChunks> = TextChunksTlbCombinator(n)
    }
}

private class TextChunksTlbCombinator(
    n: Int
) : TlbCombinator<TextChunks>() {
    val constructor = if (n == 0) {
        TextChunkEmpty.tlbConstructor()
    } else {
        TextChunk.tlbConstructor(n - 1)
    }
    override val constructors: List<TlbConstructor<out TextChunks>>
        get() = listOf(constructor)
}
