package org.ton.lite.api.exception

import kotlin.jvm.JvmStatic

sealed class LiteServerException constructor(
    val code: Int,
    override val message: String
) : RuntimeException() {
    companion object {
        @JvmStatic
        fun create(code: Int, message: String): LiteServerException = when(code) {
            LiteServerCancelledException.ERROR_CODE -> LiteServerCancelledException(message)
            LiteServerFailureException.ERROR_CODE -> LiteServerFailureException(message)
            LiteServerErrorException.ERROR_CODE -> LiteServerErrorException(message)
            LiteServerWarningException.ERROR_CODE -> LiteServerWarningException(message)
            LiteServerProtoviolationException.ERROR_CODE -> LiteServerProtoviolationException(message)
            LiteServerTimeoutException.ERROR_CODE -> LiteServerTimeoutException(message)
            LiteServerNotReadyException.ERROR_CODE -> LiteServerNotReadyException(message)
            else -> LiteServerUnknownException(code, message)
        }
    }
}

class LiteServerFailureException(
    message: String
) : LiteServerException(ERROR_CODE, message) {
    companion object {
        const val ERROR_CODE = 601
    }
}

class LiteServerErrorException(
    message: String
) : LiteServerException(ERROR_CODE, message) {
    companion object {
        const val ERROR_CODE = 602
    }
}

class LiteServerWarningException(
    message: String
) : LiteServerException(ERROR_CODE, message) {
    companion object {
        const val ERROR_CODE = 603
    }
}

class LiteServerProtoviolationException(
    message: String
) : LiteServerException(ERROR_CODE, message) {
    companion object {
        const val ERROR_CODE = 621
    }
}

class LiteServerNotReadyException(
    message: String
) : LiteServerException(ERROR_CODE, message) {
    companion object {
        const val ERROR_CODE = 651
    }
}

class LiteServerTimeoutException(
    message: String
) : LiteServerException(ERROR_CODE, message) {
    companion object {
        const val ERROR_CODE = 652
    }
}

class LiteServerCancelledException(
    message: String
) : LiteServerException(ERROR_CODE, message) {
    companion object {
        const val ERROR_CODE = 653
    }
}

class LiteServerUnknownException(
    code: Int,
    message: String
) : LiteServerException(code, message)
