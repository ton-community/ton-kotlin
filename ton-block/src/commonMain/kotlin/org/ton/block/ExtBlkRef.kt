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
    val end_lt: Long,
    val seq_no: Long,
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
        storeUInt(value.end_lt, 64)
        storeUInt(value.seq_no, 32)
        storeBits(value.root_hash)
        storeBits(value.file_hash)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): ExtBlkRef = cellSlice {
        val endLt = loadUInt(64).toLong()
        val seqNo = loadUInt(32).toLong()
        val rootHash = loadBitString(256)
        val fileHash = loadBitString(256)
        ExtBlkRef(endLt, seqNo, rootHash, fileHash)
    }
}