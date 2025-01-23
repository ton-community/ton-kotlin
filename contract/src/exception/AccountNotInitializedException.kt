package org.ton.contract.exception

import org.ton.block.message.address.AddrInt

public class AccountNotInitializedException(
    public val address: AddrInt
) : RuntimeException("Account not initialized: $address")
