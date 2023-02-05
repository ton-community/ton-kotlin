package org.ton.tonlib.types

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("options")
internal data class Options(
    val config: Config,
    val keystoreType: KeyStoreType
)
