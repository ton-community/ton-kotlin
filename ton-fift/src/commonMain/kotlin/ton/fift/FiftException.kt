package ton.fift

import ton.types.ExceptionCode

open class FiftException(
    exceptionCode: ExceptionCode,
    message: String? = null,
) : Exception("Fift Exception: $exceptionCode, code: ${exceptionCode.code}${if (!message.isNullOrBlank()) ", $message" else ""}")

class FiftStackOverflow(message: String? = null) : FiftException(ExceptionCode.StackOverflow, message)