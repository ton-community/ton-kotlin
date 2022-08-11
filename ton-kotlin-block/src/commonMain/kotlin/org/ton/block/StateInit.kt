package org.ton.block

import kotlinx.serialization.Serializable
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.hashmap.EmptyHashMapE
import org.ton.hashmap.HashMapE
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.constructor.UIntTlbConstructor
import org.ton.tlb.constructor.tlbCodec
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@Serializable
data class StateInit(
    val split_depth: Maybe<Int>,
    val special: Maybe<TickTock>,
    val code: Maybe<Cell>,
    val data: Maybe<Cell>,
    val library: HashMapE<SimpleLib>
) {
    constructor(
        code: Cell? = null,
        data: Cell? = null,
        library: HashMapE<SimpleLib> = EmptyHashMapE(),
        splitDepth: Int? = null,
        special: TickTock? = null
    ) : this(
        splitDepth.toMaybe(), special.toMaybe(), code.toMaybe(), data.toMaybe(), library
    )

    override fun toString(): String = "split_depth:$split_depth special:$special code:$code data:$data library:$library"

    companion object : TlbCodec<StateInit> by StateInitTlbConstructor {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<StateInit> = StateInitTlbConstructor
    }
}

private object StateInitTlbConstructor : TlbConstructor<StateInit>(
    schema = "_ split_depth:(Maybe (## 5)) special:(Maybe TickTock) code:(Maybe ^Cell) data:(Maybe ^Cell) library:(HashmapE 256 SimpleLib) = StateInit;"
) {
    private val Maybe5 = Maybe(UIntTlbConstructor.int(5))
    private val MaybeTickTock = Maybe(TickTock)
    private val MaybeCell = Maybe(Cell.tlbCodec())
    private val Library = HashMapE.tlbCodec(256, SimpleLib)

    override fun storeTlb(
        cellBuilder: CellBuilder, value: StateInit
    ) = cellBuilder {
        storeTlb(Maybe5, value.split_depth)
        storeTlb(MaybeTickTock, value.special)
        storeTlb(MaybeCell, value.code)
        storeTlb(MaybeCell, value.data)
        storeTlb(Library, value.library)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): StateInit = cellSlice {
        val splitDepth = loadTlb(Maybe5)
        val special = loadTlb(MaybeTickTock)
        val code = loadTlb(MaybeCell)
        val data = loadTlb(MaybeCell)
        val library = loadTlb(Library)
        StateInit(splitDepth, special, code, data, library)
    }
}
