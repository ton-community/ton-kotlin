package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@SerialName("master_info")
@Serializable
data class BlkMasterInfo(
    val master: ExtBlkRef
) {
    companion object : TlbCodec<BlkMasterInfo> by BlkMasterInfoTlbConstructor
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