package org.ton.lite.api.liteserver

import org.ton.tl.TlConstructor
import org.ton.tl.TlReader
import org.ton.tl.TlWriter

public object LiteServerQueryPrefix : TlConstructor<LiteServerQueryPrefix>(
    schema = "liteServer.queryPrefix = Object"
) {
    override fun decode(reader: TlReader): LiteServerQueryPrefix = LiteServerQueryPrefix
    override fun encode(writer: TlWriter, value: LiteServerQueryPrefix) {}
}
