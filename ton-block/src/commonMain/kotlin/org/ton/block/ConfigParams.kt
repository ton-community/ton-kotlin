package org.ton.block

import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.cell.Cell
import org.ton.hashmap.HashMapEdge

@Serializable
data class ConfigParams(
    val config_addr: BitString,
    val config: HashMapEdge<Cell>
) {
    init {
        require(config_addr.size == 256) { "required: config_addr.size == 256, actual: ${config_addr.size}" }
    }
}