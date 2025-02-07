package org.ton.block

import kotlinx.serialization.SerialName
import org.ton.cell.Cell


@SerialName("libref_ref")
public data class LibRefRef(
    val library: Cell
) : LibRef
