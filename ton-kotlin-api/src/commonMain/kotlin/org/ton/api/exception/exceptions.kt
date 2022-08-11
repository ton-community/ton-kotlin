@file:Suppress("NOTHING_TO_INLINE")

package org.ton.api.exception

inline fun TonException(code: Int, message: String, cause: Throwable? = null) = when (code) {
    TonFailureException.CODE -> TonFailureException(message, cause)
    TonErrorException.CODE -> TonErrorException(message, cause)
    TonWarningException.CODE -> TonWarningException(message, cause)
    TonProtoviolationException.CODE -> TonProtoviolationException(message, cause)
    TonNotReadyException.CODE -> TonNotReadyException(message, cause)
    TonTimeoutException.CODE -> TonTimeoutException(message, cause)
    TonCancelledException.CODE -> TonCancelledException(message, cause)
    else -> throw IllegalArgumentException("Unknown error code: $code")
}

sealed class TonException constructor(
    override val message: String,
    override val cause: Throwable? = null
) : RuntimeException(message, cause) {
    abstract val code: Int
}

open class TonFailureException(
    override val message: String,
    override val cause: Throwable? = null
) : TonException(message, cause) {
    override val code: Int = CODE

    companion object {
        const val CODE = 601
    }
}

open class TonErrorException(
    override val message: String,
    override val cause: Throwable? = null
) : TonException(message, cause) {
    override val code: Int = CODE

    companion object {
        const val CODE = 602
    }
}

open class TonWarningException(
    override val message: String,
    override val cause: Throwable? = null
) : TonException(message, cause) {
    override val code: Int = CODE

    companion object {
        const val CODE = 603
    }
}

open class TonProtoviolationException(
    override val message: String,
    override val cause: Throwable? = null
) : TonException(message, cause) {
    override val code: Int = CODE

    companion object {
        const val CODE = 621
    }
}

open class TonNotReadyException(
    override val message: String,
    override val cause: Throwable? = null
) : TonException(message, cause) {
    override val code: Int = CODE

    companion object {
        const val CODE = 651
    }
}

open class TonTimeoutException(
    override val message: String,
    override val cause: Throwable? = null
) : TonException(message, cause) {
    override val code: Int = CODE

    companion object {
        const val CODE = 652
    }
}

open class TonCancelledException(
    override val message: String,
    override val cause: Throwable? = null
) : TonException(message, cause) {
    override val code: Int = CODE

    companion object {
        const val CODE = 653
    }
}
