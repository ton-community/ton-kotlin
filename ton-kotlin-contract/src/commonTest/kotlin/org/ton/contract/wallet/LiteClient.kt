package org.ton.contract.wallet

import kotlinx.coroutines.GlobalScope
import org.ton.api.liteclient.config.LiteClientConfigGlobal
import org.ton.api.liteserver.LiteServerDesc
import org.ton.api.pub.PublicKeyEd25519
import org.ton.crypto.base64
import org.ton.lite.client.LiteClient

fun liteClient() = LiteClient(
    GlobalScope.coroutineContext,
    LiteClientConfigGlobal(
        liteservers = listOf(
            LiteServerDesc(
                id = PublicKeyEd25519(base64("R1KsqYlNks2Zows+I9s4ywhilbSevs9dH1x2KF9MeSU=")),
                ip = 1426768764,
                port = 13724
            )
        )
    )
)
