package org.ton.lite.api.liteserver.internal

import io.ktor.utils.io.core.*
import org.ton.boc.BagOfCells
import org.ton.tl.TlReader
import org.ton.tl.TlWriter

internal inline fun TlWriter.writeBoc(boc: BagOfCells) =
    writeBytes(buildPacket {
        boc.write(this)
    }.readBytes())

internal inline fun TlReader.readBoc(): BagOfCells =
    BagOfCells.read(ByteReadPacket(readBytes()))
