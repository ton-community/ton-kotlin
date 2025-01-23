package org.ton.contract

import org.ton.block.message.address.AddrStd

public interface SmartContract {
    public val address: AddrStd

    public val provider: Provider
}
