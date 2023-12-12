package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.Cell

@Serializable
@SerialName("libref_ref")
data class LibRefRef(
    val library: Cell
) : LibRef
