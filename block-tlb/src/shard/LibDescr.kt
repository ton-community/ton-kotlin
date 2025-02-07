package org.ton.kotlin.shard

import org.ton.kotlin.cell.Cell
import org.ton.kotlin.cell.CellBuilder
import org.ton.kotlin.cell.CellContext
import org.ton.kotlin.cell.CellSlice
import org.ton.kotlin.cell.serialization.CellSerializer
import org.ton.kotlin.dict.RawDictionary
import org.ton.kotlin.message.address.StdAddr

/**
 * Shared libraries currently can be present only in masterchain blocks.
 */
public data class LibDescr(
    /**
     * Library code.
     */
    val lib: Cell,

    /**
     * Accounts in the masterchain that store this library.
     */
    val publishers: Publishers
) {
    public class Publishers(
        public val dict: RawDictionary
    ) : Iterable<StdAddr> {
        override fun iterator(): Iterator<StdAddr> = dict.asSequence().map { (addr, _) ->
            StdAddr(-1, addr)
        }.iterator()

        public fun contains(addr: StdAddr): Boolean = addr.workchain == -1 && dict.contains(addr.address)
    }

    public companion object {
        public fun cellSerializer(): CellSerializer<LibDescr> = LibDescrSerializer
    }
}

private object LibDescrSerializer : CellSerializer<LibDescr> {
    override fun load(slice: CellSlice, context: CellContext): LibDescr {
        val lib = slice.loadRef()
        val publishers = LibDescr.Publishers(RawDictionary.loadFromSlice(slice, 256, context))
        return LibDescr(lib, publishers)
    }

    override fun store(
        builder: CellBuilder,
        value: LibDescr,
        context: CellContext
    ) {
        builder.storeRef(value.lib)
        value.publishers.dict.root?.let {
            builder.storeSlice(context.loadCell(it).asCellSlice())
        }
    }
}