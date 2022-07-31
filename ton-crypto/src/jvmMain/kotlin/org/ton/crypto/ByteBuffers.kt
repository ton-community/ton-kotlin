package org.ton.crypto

import io.ktor.utils.io.pool.*
import java.nio.ByteBuffer

internal val EmptyByteBuffer: ByteBuffer = ByteBuffer.allocate(0)
internal val DefaultByteBufferPool: ObjectPool<ByteBuffer> = DirectByteBufferPool(4096, 4096)
internal val CryptoBufferPool: ObjectPool<ByteBuffer> = ByteBufferPool(128, 65536)
