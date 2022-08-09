package org.ton.crypto

import io.ktor.utils.io.core.*

expect fun sha256(vararg bytes: ByteArray): ByteArray
expect fun sha256(builder: BytePacketBuilder.() -> Unit): ByteArray
