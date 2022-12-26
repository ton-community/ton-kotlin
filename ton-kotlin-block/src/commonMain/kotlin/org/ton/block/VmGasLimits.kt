@file:Suppress("NOTHING_TO_INLINE")

package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SerialName("gas_limits")
@Serializable
data class VmGasLimits(
    val remaining: Long = Long.MAX_VALUE,
    val maxLimit: Long = Long.MAX_VALUE,
    val curLimit: Long = Long.MAX_VALUE,
    val credit: Long = Long.MAX_VALUE
) {
    public fun consume(amount: Int): VmGasLimits = consume(amount.toLong())
    public fun consume(amount: Long): VmGasLimits = copy(
        remaining = remaining - amount,
    )

    public fun consumeOrNull(amount: Int): VmGasLimits? = consumeOrNull(amount.toLong())
    public fun consumeOrNull(amount: Long): VmGasLimits? = if (amount <= remaining) {
        consume(amount)
    } else {
        null
    }
}
