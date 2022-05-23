package org.ton.tlb.exception

import org.ton.bitstring.BitString

class UnknownTlbConstructorException(
    val id: BitString? = null
) : IllegalArgumentException(if (id != null) "Unknown constructor: $id" else "Unknown constructor")
