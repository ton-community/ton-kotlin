package ton.block

import kotlinx.serialization.Serializable
import ton.cell.Cell

@Serializable
data class SimpleLib(
    val public: Boolean,
    val root: Cell
)