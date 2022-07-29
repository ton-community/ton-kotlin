package org.ton.api.exception

class TvmException(
    val code: Int,
    message: String? = null,
    cause: Throwable? = null
) : RuntimeException(if (message != null) code.toString() else "[$code] $message", cause)
