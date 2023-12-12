package org.ton.tlb.exception

import org.ton.bitstring.BitString

public class UnknownTlbConstructorException(
    public val id: BitString? = null
) : IllegalArgumentException(if (id != null) "Unknown constructor: $id (${id.toBinary()})" else "Unknown constructor")
