package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@SerialName("merkle_update")
@Serializable
data class MerkleUpdate<X : Any>(
    val old_hash: BitString,
    val new_hash: BitString,
    val old: X,
    val new: X
) {
    init {
        require(old_hash.size == 256) { "required: old_hash.size = 256, actual: ${old_hash.size}" }
        require(new_hash.size == 256) { "required: new_hash.size = 256, actual: ${new_hash.size}" }
    }

    companion object {
        @JvmStatic
        fun <X : Any> tlbCodec(
            x: TlbCodec<X>
        ): TlbConstructor<MerkleUpdate<X>> = MerkleUpdateTlbConstructor(x)
    }
}

private class MerkleUpdateTlbConstructor<X : Any>(
    val x: TlbCodec<X>
) : TlbConstructor<MerkleUpdate<X>>(
    schema = "!merkle_update#02 {X:Type} old_hash:bits256 new_hash:bits256 old:^X new:^X = MERKLE_UPDATE X;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: MerkleUpdate<X>
    ) = cellBuilder {
        storeBits(value.old_hash)
        storeBits(value.new_hash)
        storeRef {
            storeTlb(x, value.old)
        }
        storeRef {
            storeTlb(x, value.new)
        }
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): MerkleUpdate<X> = cellSlice {
        val oldHash = loadBitString(256)
        val newHash = loadBitString(256)
        val old = loadRef { loadTlb(x) }
        val new = loadRef { loadTlb(x) }
        MerkleUpdate(oldHash, newHash, old, new)
    }
}
