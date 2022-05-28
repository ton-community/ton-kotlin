package org.ton.block

import kotlinx.serialization.Serializable
import org.ton.cell.Cell

@Serializable
data class SimpleLib(
        val public: Boolean,
        val root: Cell
)
