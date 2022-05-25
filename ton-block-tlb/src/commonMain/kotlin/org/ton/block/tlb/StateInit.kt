package org.ton.block.tlb

import org.ton.block.Maybe
import org.ton.block.SimpleLib
import org.ton.block.StateInit
import org.ton.block.TickTock
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.hashmap.HashMapE
import org.ton.hashmap.tlb.tlbCodec
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.constructor.UIntTlbConstructor
import org.ton.tlb.constructor.tlbCodec
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

fun StateInit.Companion.tlbCodec(): TlbCodec<StateInit> = StateInitTlbConstructor()

private class StateInitTlbConstructor : TlbConstructor<StateInit>(
    schema = "_ split_depth:(Maybe (## 5)) special:(Maybe TickTock) code:(Maybe ^Cell) data:(Maybe ^Cell) library:(HashmapE 256 SimpleLib) = StateInit;"
) {
    private val maybeUint5Codec by lazy {
        Maybe.tlbCodec(UIntTlbConstructor.int(5))
    }
    private val maybeTickTockCodec by lazy {
        Maybe.tlbCodec(TickTock.tlbCodec())
    }
    private val maybeCell by lazy {
        Maybe.tlbCodec(Cell.tlbCodec())
    }
    private val hashMapESimpleLibCodec by lazy {
        HashMapE.tlbCodec(256, SimpleLib.tlbCodec())
    }

    override fun storeTlb(
        cellBuilder: CellBuilder, value: StateInit
    ) = cellBuilder {
        storeTlb(maybeUint5Codec, value.splitDepth)
        storeTlb(maybeTickTockCodec, value.special)
        storeTlb(maybeCell, value.code)
        storeTlb(maybeCell, value.data)
        storeTlb(hashMapESimpleLibCodec, value.library)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): StateInit = cellSlice {
        val splitDepth = loadTlb(maybeUint5Codec)
        val special = loadTlb(maybeTickTockCodec)
        val code = loadTlb(maybeCell)
        val data = loadTlb(maybeCell)
        val library = loadTlb(hashMapESimpleLibCodec)
        StateInit(splitDepth, special, code, data, library)
    }
}
