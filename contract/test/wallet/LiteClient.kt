package org.ton.contract.wallet

import kotlinx.coroutines.Dispatchers
import org.ton.lite.client.LiteClient

fun liteClientTestnet() = LiteClient(
    liteClientConfigGlobal = TESTNET_GLOBAL_CONFIG,
    coroutineContext = Dispatchers.Default
)
