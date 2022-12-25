package org.ton.tl

import io.ktor.utils.io.core.*

public interface TlDecoder<T> {
    public fun decode(byteArray: ByteArray): T = decode(ByteReadPacket(byteArray))
    public fun decode(input: Input): T = decode(TlReader(input))
    public fun decode(reader: TlReader): T

    public fun decodeBoxed(byteArray: ByteArray): T = decodeBoxed(ByteReadPacket(byteArray))
    public fun decodeBoxed(input: Input): T = decodeBoxed(TlReader(input))
    public fun decodeBoxed(reader: TlReader): T
}
