@file:Suppress("OPT_IN_USAGE")

package org.ton.primitives

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@JsonClassDiscriminator("@type")
@Serializable
sealed interface Maybe<X> {
    val value: X?
}

@SerialName("nothing")
@Serializable
class Nothing<X> : Maybe<X> {
    override val value: X? = null
}

@SerialName("just")
@Serializable
class Just<X>(
        override val value: X
) : Maybe<X>

fun <X> X?.toMaybe(): Maybe<X> = if (this != null) Just(this) else Nothing()