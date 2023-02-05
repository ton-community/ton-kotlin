package org.ton.tonlib.types

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal enum class KeyStoreType {
    @SerialName("keyStoreTypeDirectory")
    DIRECTORY,

    @SerialName("keyStoreTypeInMemory")
    IN_MEMORY
}
