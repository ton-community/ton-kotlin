package org.ton.block

import org.ton.bitstring.toBitString
import kotlin.test.Test

class TextTest {
    @Test
    fun foo() {

        val string =
            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaabbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbcccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccdddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeefffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff"

        val chunks = string.encodeToByteArray().toList().chunked(127).reversed()
        var next: TextChunk? = null
        chunks.forEach { chunk ->
            val textChunk = TextChunk(
                chunk.size.toUByte(),
                chunk.toByteArray().toBitString(),
                next?.let { ChunkRef(it) } ?: ChunkRefEmpty
            )
            next = textChunk
        }
        val result = Text(chunks.size.toUByte(), next ?: TextChunkEmpty)
    }
}
