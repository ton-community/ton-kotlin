package ton.types.block

import kotlinx.serialization.Serializable

@Serializable
data class TickTock(
    val tick: Boolean,
    val tock: Boolean
)