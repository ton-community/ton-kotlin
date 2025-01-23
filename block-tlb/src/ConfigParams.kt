package org.ton.block

import org.ton.bitstring.BitString
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.hashmap.HmEdge
import org.ton.tlb.CellRef
import org.ton.tlb.TlbCodec
import org.ton.tlb.constructor.tlbCodec
import org.ton.tlb.loadRef
import org.ton.tlb.storeRef

/**
 * Blockchain config.
 */
public data class ConfigParams(
    /**
     * Configuration contract address.
     */
    val address: BitString,

    /**
     * Configuration parameters.
     */
    val params: CellRef<HmEdge<Cell>>
) {
    init {
        require(address.size == 256)
    }

    public object Tlb : TlbCodec<ConfigParams> {
        private val hashmap = HmEdge.tlbCodec(32, Cell.tlbCodec())

        override fun storeTlb(
            cellBuilder: CellBuilder,
            value: ConfigParams
        ): Unit = cellBuilder {
            storeBits(value.address)
            storeRef(hashmap, value.params)
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): ConfigParams = cellSlice {
            val configAddr = loadBits(256)
            val config = loadRef(hashmap)
            ConfigParams(configAddr, config)
        }
    }
}

