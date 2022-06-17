package org.ton.cell.exception

class CellOverflowException : RuntimeException {
    constructor(message: String) : super(message)
    constructor(cause: Throwable) : super("Cell overflow", cause)
    constructor(message: String, cause: Throwable) : super(message, cause)
}

class CellUnderflowException : RuntimeException {
    constructor(message: String) : super(message)
    constructor(cause: Throwable) : super("Cell underflow", cause)
    constructor(message: String, cause: Throwable) : super(message, cause)
}
