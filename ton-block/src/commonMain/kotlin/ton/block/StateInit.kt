package ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ton.cell.Cell
import ton.hashmap.HashMapE

@SerialName("state_init")
@Serializable
data class StateInit(
    val split_depth: Int?,
    val special: TickTock?,
    val code: Cell?,
    val data: Cell?,
    val library: HashMapE<SimpleLib>
)