package org.ton.block

import org.ton.cell.Cell
import org.ton.hashmap.HashMapE


public data class VmLibraries(
    val libraries: HashMapE<Cell>
)
