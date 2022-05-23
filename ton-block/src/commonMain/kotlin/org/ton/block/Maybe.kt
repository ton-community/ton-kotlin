@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@JsonClassDiscriminator("@type")
@Serializable
sealed interface Maybe<X : Any> {
    val value: X?
}

@SerialName("nothing")
@Serializable
class Nothing<X : Any> : Maybe<X> {
    override val value: X? = null
}

@SerialName("just")
@Serializable
class Just<X : Any>(
    override val value: X
) : Maybe<X>

fun <X : Any> X?.toMaybe(): Maybe<X> = if (this != null) Just(this) else Nothing()
