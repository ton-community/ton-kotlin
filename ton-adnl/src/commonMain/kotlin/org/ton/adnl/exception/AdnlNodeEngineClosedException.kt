package org.ton.adnl.exception

class AdnlNodeEngineClosedException(
    override val cause: Throwable? = null
) : IllegalStateException("ADNL Node already closed")