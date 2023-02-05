package org.ton.tonlib.functions

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.tonlib.types.Options

@Serializable
@SerialName("init")
internal data class Init(
    val options: Options
)
