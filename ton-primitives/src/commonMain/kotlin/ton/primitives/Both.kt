package ton.primitives

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SerialName("pair")
@Serializable
data class Both<X, Y>(
    val first: X,
    val second: Y
)

fun <X, Y> Pair<X, Y>.toBoth(): Both<X, Y> = Both(first, second)
fun <X, Y> Both<X, Y>.toPair(): Pair<X, Y> = first to second