package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.Cell
import org.ton.hashmap.HashMapE

@SerialName("state_init")
@Serializable
data class StateInit(
    @SerialName("split_depth")
    val splitDepth: Maybe<Int>,
    val special: Maybe<TickTock>,
    val code: Maybe<Cell>,
    val data: Maybe<Cell>,
    val library: HashMapE<SimpleLib>
) {
    constructor(
        library: HashMapE<SimpleLib>,
        splitDepth: Int? = null,
        special: TickTock? = null,
        code: Cell? = null,
        data: Cell? = null
    ) : this(
        splitDepth.toMaybe(), special.toMaybe(), code.toMaybe(), data.toMaybe(), library
    )
}
