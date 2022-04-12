package ton.types.block

import kotlinx.serialization.Serializable

@Serializable
data class CurrencyCollection(
    val grams: Grams,
    val other: ExtraCurrencyCollection
)