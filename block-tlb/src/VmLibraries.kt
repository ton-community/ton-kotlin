package org.ton.block

import kotlinx.serialization.Serializable
import org.ton.cell.Cell
import org.ton.hashmap.HashMapE

@Serializable
public data class VmLibraries(
    val libraries: HashMapE<Cell>
)
