package org.ton.adnl.exception

public class AdnlOldPacketSeqnoException(
    public val packetSeqno: Long,
    public val localSeqno: Long
) : RuntimeException("Old packet seqno: $packetSeqno (current max $localSeqno)")
