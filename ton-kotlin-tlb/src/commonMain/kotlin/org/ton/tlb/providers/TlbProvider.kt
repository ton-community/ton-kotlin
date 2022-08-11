package org.ton.tlb.providers

import org.ton.tlb.TlbCodec

sealed interface TlbProvider<T> : TlbCodec<T>