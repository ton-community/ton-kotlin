package org.ton.tl

import kotlinx.io.Buffer
import kotlinx.io.Sink
import kotlinx.io.bytestring.ByteString
import kotlinx.io.readByteArray

public interface TlEncoder<in T> {
    public fun encode(sink: Sink, value: T): Unit = encode(TlWriter(sink), value)
    public fun encode(writer: TlWriter, value: T)

    public fun encodeBoxed(sink: Sink, value: T): Unit = encodeBoxed(TlWriter(sink), value)
    public fun encodeBoxed(writer: TlWriter, value: T)
    public fun encodeToByteArray(value: T, boxed: Boolean = true): ByteArray {
        val buffer = Buffer()
        if (boxed) encodeBoxed(buffer, value) else encode(buffer, value)
        return buffer.readByteArray()
    }

    public fun encodeToByteString(value: T, boxed: Boolean = true): ByteString =
        ByteString(*encodeToByteArray(value, boxed))

    public fun hash(value: T): ByteArray =
        io.github.andreypfau.kotlinx.crypto.sha2.sha256(encodeToByteArray(value))
}

@Suppress("UNCHECKED_CAST", "NOTHING_TO_INLINE")
internal inline fun <T> TlEncoder<*>.cast(): TlEncoder<T> = this as TlEncoder<T>
