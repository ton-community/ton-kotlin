package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor

@SerialName("ext_blk_ref")
@Serializable
data class ExtBlkRef(
    val end_lt: ULong,
    val seq_no: UInt,
    val root_hash: BitString,
    val file_hash: BitString
) {
    init {
        require(root_hash.size == 256) { "required: root_hash.size = 256, actual: ${root_hash.size}" }
        require(file_hash.size == 256) { "required: fileHash.size = 256, actual: ${file_hash.size}" }
    }

    companion object : TlbCodec<ExtBlkRef> by ExtBlkRefTlbConstructor
}

private object ExtBlkRefTlbConstructor : TlbConstructor<ExtBlkRef>(
    schema = "ext_blk_ref\$_ end_lt:uint64 " +
            "seq_no:uint32 root_hash:bits256 file_hash:bits256 " +
            "= ExtBlkRef;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: ExtBlkRef
    ) = cellBuilder {
        storeUInt64(value.end_lt)
        storeUInt32(value.seq_no)
        storeBits(value.root_hash)
        storeBits(value.file_hash)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): ExtBlkRef = cellSlice {
        val endLt = loadUInt64()
        val seqNo = loadUInt32()
        val rootHash = loadBits(256)
        val fileHash = loadBits(256)
        ExtBlkRef(endLt, seqNo, rootHash, fileHash)
    }
}
