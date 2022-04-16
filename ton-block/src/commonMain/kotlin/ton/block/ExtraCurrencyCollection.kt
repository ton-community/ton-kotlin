package ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ton.hashmap.HashMapE

@Serializable
@SerialName("extra_currencies")
data class ExtraCurrencyCollection(
    val dict: HashMapE<VarUInteger>
)