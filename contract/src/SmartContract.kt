package org.ton.contract

import org.ton.kotlin.message.address.StdAddr

public interface SmartContract {
    public val address: StdAddr

    public val provider: Provider
}
