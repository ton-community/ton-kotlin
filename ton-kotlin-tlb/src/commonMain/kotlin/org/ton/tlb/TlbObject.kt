package org.ton.tlb

public interface TlbObject {
    public fun print(printer: TlbPrettyPrinter = TlbPrettyPrinter(indent = 2)): TlbPrettyPrinter
}
