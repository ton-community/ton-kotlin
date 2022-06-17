package org.ton.tl

data class TlField<T : Any>(
    val name: String,
    val constructor: TlCodec<T>
) : TlCodec<T> by constructor