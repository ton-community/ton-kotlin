package org.ton.adnl.exception

import org.ton.api.adnl.AdnlIdShort

public class UnknownAdnlDestinationException(
    public val destination: AdnlIdShort
) : RuntimeException("Unknown ADNL destination: $destination")
