package org.ton.tl

class TlException(
    override val message: String?,
    override val cause: Throwable? = null
) : RuntimeException()