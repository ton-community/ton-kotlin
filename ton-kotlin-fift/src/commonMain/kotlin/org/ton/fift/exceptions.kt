package org.ton.fift

// TODO: exception code
open class FiftException(
    exceptionCode: Int,
    message: String? = null,
) : Exception("Fift Exception: $exceptionCode${if (!message.isNullOrBlank()) ", $message" else ""}")

class FiftStackOverflow(message: String? = null) : FiftException(0, message)
