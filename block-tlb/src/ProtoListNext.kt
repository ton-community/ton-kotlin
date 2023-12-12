package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.tlb.loadTlb
import org.ton.tlb.providers.TlbConstructorProvider
import org.ton.tlb.storeTlb

@SerialName("proto_list_next")
@Serializable
public data class ProtoListNext(
    val head: Protocol,
    val tail: ProtoList
) : ProtoList {
    override fun iterator(): Iterator<Protocol> = iterator {
        yield(head)
        yieldAll(tail)
    }

    public companion object : TlbConstructorProvider<ProtoListNext> by ProtoListNextTlbConstructor
}

private object ProtoListNextTlbConstructor : org.ton.tlb.TlbConstructor<ProtoListNext>(
    schema = "proto_list_next#1 head:Protocol tail:ProtoList = ProtoList;"
) {
    override fun storeTlb(
        cellBuilder: org.ton.cell.CellBuilder,
        value: ProtoListNext
    ) {
        cellBuilder.storeTlb(Protocol, value.head)
        cellBuilder.storeTlb(ProtoList, value.tail)
    }

    override fun loadTlb(
        cellSlice: org.ton.cell.CellSlice
    ): ProtoListNext {
        val head = cellSlice.loadTlb(Protocol)
        val tail = cellSlice.loadTlb(ProtoList)
        return ProtoListNext(head, tail)
    }
}
