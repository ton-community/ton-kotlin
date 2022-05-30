package org.ton.bitstring.exception

class BitStringOverflowException(
    override val message: String? = "BitString overflow"
) : RuntimeException()

class BitStringUnderflowException : RuntimeException("BitString underflow")
