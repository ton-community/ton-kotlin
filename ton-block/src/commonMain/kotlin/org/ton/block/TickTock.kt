package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SerialName("tick_tock")
@Serializable
data class TickTock(
    val tick: Boolean,
    val tock: Boolean
) {
    fun toPair(): Pair<Boolean, Boolean> = tick to tock
}

fun Pair<Boolean, Boolean>.toTickTock(): TickTock = TickTock(first, second)
