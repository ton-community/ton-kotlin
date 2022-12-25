package org.ton.tlb.providers

import org.ton.tlb.TlbCodec

public sealed interface TlbProvider<T> : TlbCodec<T>
