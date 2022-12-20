package org.ton.tl

import io.ktor.utils.io.core.*
import org.ton.crypto.sha256

public interface TlEncoder<in T> {
    public fun encode(output: Output, value: T): Unit = encode(TlWriter(output), value)
    public fun encode(writer: TlWriter, value: T)

    public fun encodeBoxed(output: Output, value: T): Unit = encodeBoxed(TlWriter(output), value)
    public fun encodeBoxed(writer: TlWriter, value: T)
    public fun encodeToByteArray(value: T, boxed: Boolean = true): ByteArray {
        val output = BytePacketBuilder()
        if (boxed) encodeBoxed(output, value) else encode(output, value)
        return output.build().readBytes()
    }

    public fun hash(value: T): ByteArray =
        sha256(encodeToByteArray(value))
}

@Suppress("UNCHECKED_CAST", "NOTHING_TO_INLINE")
internal inline fun <T> TlEncoder<*>.cast(): TlEncoder<T> = this as TlEncoder<T>
