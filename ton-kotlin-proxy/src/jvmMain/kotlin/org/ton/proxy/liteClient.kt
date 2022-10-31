package org.ton.proxy

import kotlinx.serialization.json.Json
import org.ton.api.liteserver.LiteServerDesc
import org.ton.api.pub.PublicKeyEd25519
import org.ton.crypto.base64
import org.ton.lite.client.LiteClient

internal val JSON = Json {
    classDiscriminator = "@type"
    ignoreUnknownKeys = true
}
internal val LITE_CLIENT = LiteClient(
    LiteServerDesc(
        id = PublicKeyEd25519(base64("n4VDnSCUuSpjnCyUk9e3QOOd6o0ItSWYbTnW3Wnn8wk=")),
        ip = 84478511,
        port = 19949
    )
)
