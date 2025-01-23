package org.ton.block.account

import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.hashmap.HashMapE
import org.ton.hashmap.HmeRoot
import org.ton.tlb.*

/**
 * Deployed account state.
 */
public data class StateInit(
    /**
     * Optional split depth for large smart contracts.
     */
    val splitDepth: SplitDepth? = null,

    /**
     * Optional special contract flags.
     */
    val special: TickTock? = null,

    /**
     * Optional contract code.
     */
    val code: Cell? = null,

    /**
     * Optional contract data.
     */
    val data: Cell? = null,

    /**
     * Libraries used in smart-contract.
     */
    val libraries: HashMapE<SimpleLib> = HashMapE.of()
) {
    public constructor(
        code: Cell? = null,
        data: Cell? = null,
        library: HashMapE<SimpleLib> = HashMapE.of(),
        splitDepth: SplitDepth? = null,
        special: TickTock? = null
    ) : this(
        splitDepth,
        special,
        code,
        data,
        library
    )

    /**
     * Number of data bits that this struct occupies.
     */
    val bitLength: Int
        get() =
            1 + (if (splitDepth != null) 1 else 0) * SplitDepth.BITS +
                    (1 + (if (special != null) 1 else 0) * TickTock.BITS) +
                    3

    /**
     * Returns the number of references that this struct occupies.
     */
    val referenceCount: Int
        get() =
            if (code != null) 1 else 0 + if (data != null) 1 else 0 + if (libraries is HmeRoot<*>) 1 else 0

    public object Tlb : TlbCodec<StateInit> {
        private val Library = HashMapE.tlbCodec(256, SimpleLib.Tlb)

        override fun storeTlb(
            cellBuilder: CellBuilder, value: StateInit
        ): Unit = cellBuilder {
            storeNullableTlb(SplitDepth.Tlb, value.splitDepth)
            storeNullableTlb(TickTock.Tlb, value.special)
            if (value.code != null) {
                storeBit(true)
                storeRef(value.code)
            } else {
                storeBit(false)
            }
            if (value.data != null) {
                storeBit(true)
                storeRef(value.data)
            } else {
                storeBit(false)
            }
            storeTlb(Library, value.libraries)
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): StateInit = cellSlice {
            val splitDepth = loadNullableTlb(SplitDepth.Tlb)
            val special = loadNullableTlb(TickTock.Tlb)
            val code = if (loadBit()) {
                loadRef()
            } else null
            val data = if (loadBit()) {
                loadRef()
            } else null
            val library = loadTlb(Library)
            StateInit(splitDepth, special, code, data, library)
        }
    }
}
