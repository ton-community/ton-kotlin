package org.ton.cell.exception

class CellOverflowException(
    override val cause: Throwable? = null
) : RuntimeException("Cell overflow")

class CellUnderflowException(
    override val cause: Throwable? = null
) : RuntimeException("Cell underflow")
