package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.*
import kotlin.jvm.JvmStatic

@SerialName("merkle_update")
@Serializable
public data class MerkleUpdate<X>(
    val oldHash: BitString,
    val newHash: BitString,
    val old: CellRef<X>,
    val new: CellRef<X>
) {
    init {
        require(oldHash.size == 256) { "required: old_hash.size = 256, actual: ${oldHash.size}" }
        require(newHash.size == 256) { "required: new_hash.size = 256, actual: ${newHash.size}" }
    }

    public companion object {
        @JvmStatic
        public fun <X> tlbCodec(
            x: TlbCodec<X>
        ): TlbCodec<MerkleUpdate<X>> = MerkleUpdateTlbConstructor(x)
    }
}

private class MerkleUpdateTlbConstructor<X>(
    x: TlbCodec<X>
) : TlbConstructor<MerkleUpdate<X>>(
    schema = "!merkle_update#02 {X:Type} old_hash:bits256 new_hash:bits256 old:^X new:^X = MERKLE_UPDATE X;"
) {
    val xCellRef = CellRef.tlbCodec(x)

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: MerkleUpdate<X>
    ) = cellBuilder {
        storeBits(value.oldHash)
        storeBits(value.newHash)
        storeTlb(xCellRef, value.old)
        storeTlb(xCellRef, value.new)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): MerkleUpdate<X> = cellSlice {
        val oldHash = loadBits(256)
        val newHash = loadBits(256)
        val old = loadTlb(xCellRef)
        val new = loadTlb(xCellRef)
        // TODO: hash check
        MerkleUpdate(oldHash, newHash, old, new)
    }
}
