package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.Cell
import org.ton.hashmap.HashMapEdge

@Serializable
@SerialName("shared_lib_descr")
data class LibDescr(
    val lib: Cell,
    val publishers: HashMapEdge<Unit>
)
