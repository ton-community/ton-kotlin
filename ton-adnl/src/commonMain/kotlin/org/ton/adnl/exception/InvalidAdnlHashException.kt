package org.ton.adnl.exception

class InvalidAdnlHashException(
    override val message: String
) : IllegalStateException(message)