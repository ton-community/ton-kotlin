package org.ton.contract.wallet

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import org.ton.api.liteclient.config.LiteClientConfigGlobal
import org.ton.api.liteserver.LiteServerDesc
import org.ton.api.pub.PublicKeyEd25519
import org.ton.crypto.base64
import org.ton.lite.client.LiteClient

fun liteClient() = LiteClient(
    liteClientConfigGlobal = LiteClientConfigGlobal(
        liteServers = listOf(
            LiteServerDesc(
                id = PublicKeyEd25519(base64("n4VDnSCUuSpjnCyUk9e3QOOd6o0ItSWYbTnW3Wnn8wk=")),
                ip = 84478511,
                port = 19949
            )
        )
    ),
    coroutineContext = Dispatchers.Default
)
