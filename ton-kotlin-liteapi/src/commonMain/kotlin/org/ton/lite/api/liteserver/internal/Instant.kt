package org.ton.lite.api.liteserver.internal

import kotlinx.datetime.Instant
import org.ton.tl.TlReader
import org.ton.tl.TlWriter

internal fun TlReader.readInstant(): Instant = Instant.fromEpochSeconds(readInt().toUInt().toLong())
internal fun TlWriter.writeInstant(value: Instant) = writeInt(value.epochSeconds.toInt())
