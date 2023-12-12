package org.ton.lite.api.exception

import kotlin.jvm.JvmStatic

public sealed class LiteServerException constructor(
    public val code: Int,
    override val message: String
) : RuntimeException() {
    public companion object {
        @JvmStatic
        public fun create(code: Int, message: String): LiteServerException = when (code) {
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

public class LiteServerFailureException(
    message: String
) : LiteServerException(ERROR_CODE, message) {
    public companion object {
        public const val ERROR_CODE: Int = 601
    }
}

public class LiteServerErrorException(
    message: String
) : LiteServerException(ERROR_CODE, message) {
    public companion object {
        public const val ERROR_CODE: Int = 602
    }
}

public class LiteServerWarningException(
    message: String
) : LiteServerException(ERROR_CODE, message) {
    public companion object {
        public const val ERROR_CODE: Int = 603
    }
}

public class LiteServerProtoviolationException(
    message: String
) : LiteServerException(ERROR_CODE, message) {
    public companion object {
        public const val ERROR_CODE: Int = 621
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
