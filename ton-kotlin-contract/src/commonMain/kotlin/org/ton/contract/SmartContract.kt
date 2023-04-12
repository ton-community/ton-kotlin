package org.ton.contract

import org.ton.block.AddrStd
import org.ton.lite.client.LiteClient

public interface SmartContract {
    public val liteClient: LiteClient
    public val address: AddrStd
}
