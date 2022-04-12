package ton.types.block

import kotlinx.serialization.Serializable
import ton.cell.Cell

@Serializable
data class StateInit(
    val splitDepth: Int?,
    val special: TickTock?,
    val code: Cell?,
    val data: Cell?
//    val library: HashmapE // TODO
)