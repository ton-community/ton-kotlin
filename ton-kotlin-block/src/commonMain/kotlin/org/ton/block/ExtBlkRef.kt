package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.Bits256
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.TlbObject
import org.ton.tlb.TlbPrettyPrinter

@SerialName("ext_blk_ref")
@Serializable
public data class ExtBlkRef(
    @SerialName("end_lt") val endLt: ULong, // end_lt : uint64
    @SerialName("seq_no") val seqNo: UInt, // seq_no : uint32
    @SerialName("root_hash") val rootHash: Bits256, // root_hash : bits256
    @SerialName("file_hash") val fileHash: Bits256 // file_hash : bits256
) : TlbObject {
    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer.type("ext_blk_ref") {
        field("end_lt", endLt)
        field("seq_no", seqNo)
        field("root_hash", rootHash)
        field("file_hash", fileHash)
    }

    override fun toString(): String = print().toString()

    public companion object : TlbCodec<ExtBlkRef> by ExtBlkRefTlbConstructor
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
        storeUInt64(value.endLt)
        storeUInt32(value.seqNo)
        storeBits(value.rootHash.value)
        storeBits(value.fileHash.value)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): ExtBlkRef = cellSlice {
        val endLt = loadUInt64()
        val seqNo = loadUInt32()
        val rootHash = Bits256(loadBits(256))
        val fileHash = Bits256(loadBits(256))
        ExtBlkRef(endLt, seqNo, rootHash, fileHash)
    }
}
