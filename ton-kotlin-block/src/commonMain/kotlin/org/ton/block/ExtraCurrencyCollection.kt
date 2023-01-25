package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.hashmap.HashMapE
import org.ton.tlb.*
import org.ton.tlb.providers.TlbConstructorProvider

@Serializable
@SerialName("extra_currencies")
public data class ExtraCurrencyCollection(
    val dict: HashMapE<VarUInteger> = HashMapE.of()
) : TlbObject {
    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter {
        return printer.type("extra_currencies") {
            field("dict", dict)
        }
    }

    override fun toString(): String = print().toString()

    public companion object : TlbConstructorProvider<ExtraCurrencyCollection> by ExtraCurrencyCollectionTlbConstructor
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
