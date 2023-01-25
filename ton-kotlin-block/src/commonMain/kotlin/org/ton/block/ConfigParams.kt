package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.Bits256
import org.ton.cell.*
import org.ton.hashmap.HashMapEdge
import org.ton.tlb.*
import org.ton.tlb.constructor.tlbCodec
import org.ton.tlb.providers.TlbConstructorProvider

@Serializable
public data class ConfigParams(
    @SerialName("config_addr") val configAddr: Bits256,
    val config: CellRef<HashMapEdge<Cell>>
) : TlbObject {
    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer.type {
        field("config_addr", configAddr)
        field("config", config)
    }

    override fun toString(): String = print().toString()

    public companion object : TlbConstructorProvider<ConfigParams> by ConfigParamsTlbConstructor
}

private object ConfigParamsTlbConstructor : TlbConstructor<ConfigParams>(
    schema = "_ config_addr:bits256 config:^(Hashmap 32 ^Cell) = ConfigParams;"
) {
    val hashmap = HashMapEdge.tlbCodec(32, Cell.tlbCodec())

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: ConfigParams
    ) = cellBuilder {
        storeBits(value.configAddr.value)
        storeRef(hashmap, value.config)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): ConfigParams = cellSlice {
        val configAddr = Bits256(loadBits(256))
        val config = loadRef(hashmap)
        ConfigParams(configAddr, config)
    }
}
