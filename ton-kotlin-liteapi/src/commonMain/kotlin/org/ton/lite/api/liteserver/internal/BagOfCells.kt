package org.ton.lite.api.liteserver.internal

import org.ton.boc.BagOfCells
import org.ton.tl.TlReader
import org.ton.tl.TlWriter

internal inline fun TlWriter.writeBoc(boc: BagOfCells) =
    boc.write(this.output)

internal inline fun TlReader.readBoc(): BagOfCells =
    BagOfCells.read(this.input)
