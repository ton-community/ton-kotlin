package org.ton.contract.exception

import org.ton.block.MsgAddressInt

public class AccountNotInitializedException(
    public val address: MsgAddressInt
) : RuntimeException("Account not initialized: $address")
