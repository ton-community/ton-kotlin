package org.ton.contract.wallet

import io.ktor.util.*
import kotlinx.coroutines.Dispatchers
import org.ton.api.liteclient.config.LiteClientConfigGlobal
import org.ton.api.liteserver.LiteServerDesc
import org.ton.api.pub.PublicKeyEd25519
import org.ton.lite.client.LiteClient
import org.ton.tl.asByteString

fun liteClient() = LiteClient(
    liteClientConfigGlobal = LiteClientConfigGlobal(
        liteServers = listOf(
            LiteServerDesc(
                id = PublicKeyEd25519("n4VDnSCUuSpjnCyUk9e3QOOd6o0ItSWYbTnW3Wnn8wk=".decodeBase64Bytes().asByteString()),
                ip = 84478511,
                port = 19949
            )
        )
    ),
    coroutineContext = Dispatchers.Default
)
