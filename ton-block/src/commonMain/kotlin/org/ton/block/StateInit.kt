package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.Cell
import org.ton.hashmap.EmptyHashMapE
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
        code: Cell? = null,
        data: Cell? = null,
        library: HashMapE<SimpleLib> = EmptyHashMapE(),
        splitDepth: Int? = null,
        special: TickTock? = null
    ) : this(
        splitDepth.toMaybe(), special.toMaybe(), code.toMaybe(), data.toMaybe(), library
    )
}
