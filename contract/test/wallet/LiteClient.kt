package org.ton.contract.wallet

import io.ktor.util.*
import kotlinx.coroutines.Dispatchers
import org.ton.api.liteclient.config.LiteClientConfigGlobal
import org.ton.api.liteserver.LiteServerDesc
import org.ton.api.pub.PublicKeyEd25519
import org.ton.lite.client.LiteClient
import org.ton.tl.asByteString

fun liteClientTestnet() = LiteClient(
    liteClientConfigGlobal = LiteClientConfigGlobal(
        liteServers = listOf(
            LiteServerDesc(
                id = PublicKeyEd25519(
                    "B07X5mudyWG3zZu+Ad69+A3jHFkp+0mTrnX5sw+s3ZU=".decodeBase64Bytes().asByteString()
                ),
                ip = -1178753158,
                port = 15400
            )
        )
    ),
    coroutineContext = Dispatchers.Default
)
