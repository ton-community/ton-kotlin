package ton.fift

import ton.types.ExceptionCode

class FiftException(
    exceptionCode: ExceptionCode,
    message: String? = null
) : Exception("Fift Exception: $exceptionCode, code: ${exceptionCode.code}${if (!message.isNullOrBlank()) ", $message" else ""}")
