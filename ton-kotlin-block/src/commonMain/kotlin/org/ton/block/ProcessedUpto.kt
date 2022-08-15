package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

@Serializable
@SerialName("processed_upto")
data class ProcessedUpto(
    val last_msg_lt: ULong,
    val last_msg_hash: BitString
) {
    init {
        require(last_msg_hash.size == 256) { "required: last_msg_hash.size == 256, actual: ${last_msg_hash.size}" }
    }

    companion object : TlbConstructorProvider<ProcessedUpto> by ProcessedUptoTlbConstructor
}

private object ProcessedUptoTlbConstructor : TlbConstructor<ProcessedUpto>(
    schema = "processed_upto\$_ last_msg_lt:uint64 last_msg_hash:bits256 = ProcessedUpto;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: ProcessedUpto
    ) = cellBuilder {
        storeUInt64(value.last_msg_lt)
        storeBits(value.last_msg_hash)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): ProcessedUpto = cellSlice {
        val lastMsgLt = loadUInt64()
        val lastMsgHash = loadBits(256)
        ProcessedUpto(lastMsgLt, lastMsgHash)
    }
}
