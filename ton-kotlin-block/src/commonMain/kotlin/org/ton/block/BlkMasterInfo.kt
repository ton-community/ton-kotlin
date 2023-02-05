package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.*

@SerialName("master_info")
@Serializable
public data class BlkMasterInfo(
    val master: ExtBlkRef // master : ExtBlkRef
) : TlbObject {
    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer.type("master_info") {
        field("master", master)
    }

    override fun toString(): String = print().toString()

    public companion object : TlbCodec<BlkMasterInfo> by BlkMasterInfoTlbConstructor
}

private object BlkMasterInfoTlbConstructor : TlbConstructor<BlkMasterInfo>(
    schema = "master_info\$_ master:ExtBlkRef = BlkMasterInfo;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: BlkMasterInfo
    ) = cellBuilder {
        storeTlb(ExtBlkRef, value.master)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): BlkMasterInfo = cellSlice {
        val master = loadTlb(ExtBlkRef)
        BlkMasterInfo(master)
    }
}
