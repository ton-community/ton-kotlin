package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.hashmap.EmptyHashMapE
import org.ton.hashmap.HashMapE
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.constructor.UIntTlbConstructor
import org.ton.tlb.constructor.tlbCodec
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@SerialName("state_init")
@Serializable
data class StateInit(
    @SerialName("split_depth")
    val splitDepth: Maybe<Int>,
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

    companion object {
        @JvmStatic
        fun tlbCodec(): TlbCodec<StateInit> = StateInitTlbConstructor()
    }
}

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
