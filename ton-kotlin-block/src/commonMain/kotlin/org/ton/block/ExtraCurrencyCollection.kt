package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.hashmap.HashMapE
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb
import kotlin.jvm.JvmStatic

@Serializable
@SerialName("extra_currencies")
data class ExtraCurrencyCollection(
    val dict: HashMapE<VarUInteger> = HashMapE.of()
) {
    override fun toString(): String = "(extra_currencies\ndict:$dict)"

    companion object : TlbCodec<ExtraCurrencyCollection> by ExtraCurrencyCollectionTlbConstructor {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<ExtraCurrencyCollection> = ExtraCurrencyCollectionTlbConstructor
    }
}

private object ExtraCurrencyCollectionTlbConstructor : TlbConstructor<ExtraCurrencyCollection>(
    schema = "extra_currencies\$_ dict:(HashmapE 32 (VarUInteger 32)) = ExtraCurrencyCollection;"
) {
    private val hashMapE32Codec = HashMapE.tlbCodec(32, VarUInteger.tlbCodec(32))

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: ExtraCurrencyCollection
    ) = cellBuilder {
        storeTlb(hashMapE32Codec, value.dict)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): ExtraCurrencyCollection = cellSlice {
        val dict = loadTlb(hashMapE32Codec)
        ExtraCurrencyCollection(dict)
    }
}
