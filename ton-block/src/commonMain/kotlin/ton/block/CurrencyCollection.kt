package ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SerialName("currencies")
@Serializable
data class CurrencyCollection(
    val grams: Grams,
    val other: ExtraCurrencyCollection
)