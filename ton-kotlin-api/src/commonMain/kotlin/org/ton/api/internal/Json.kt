package org.ton.api.internal

import kotlinx.serialization.json.Json

internal val JSON = Json {
    prettyPrint = true
    ignoreUnknownKeys = true
    classDiscriminator = "@type"
}
