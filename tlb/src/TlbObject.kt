package org.ton.tlb

public interface TlbObject {
    public fun print(printer: TlbPrettyPrinter = TlbPrettyPrinter()): TlbPrettyPrinter
}
