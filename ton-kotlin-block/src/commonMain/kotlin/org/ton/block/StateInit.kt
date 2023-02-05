package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.hashmap.HashMapE
import org.ton.tlb.*
import org.ton.tlb.constructor.AnyTlbConstructor
import org.ton.tlb.constructor.UIntTlbConstructor
import kotlin.jvm.JvmStatic

@Serializable
public data class StateInit(
    @SerialName("split_depth")
    val splitDepth: Maybe<UInt>,
    val special: Maybe<TickTock>,
    val code: Maybe<CellRef<Cell>>,
    val data: Maybe<CellRef<Cell>>,
    val library: HashMapE<SimpleLib>
) : TlbObject {
    public constructor(
        code: Cell? = null,
        data: Cell? = null,
        library: HashMapE<SimpleLib> = HashMapE.of(),
        splitDepth: UInt? = null,
        special: TickTock? = null
    ) : this(
        splitDepth.toMaybe(),
        special.toMaybe(),
        code?.let { CellRef(cell = it, AnyTlbConstructor) }.toMaybe(),
        data?.let { CellRef(cell = it, AnyTlbConstructor) }.toMaybe(),
        library
    )

    override fun toString(): String = print().toString()

    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer {
        type {
            field("split_depth", splitDepth)
            field("special", special)
            field("code", code)
            field("data", data)
            field("library", library)
        }
    }

    public companion object : TlbCodec<StateInit> by StateInitTlbConstructor {
        @JvmStatic
        public fun tlbCodec(): TlbConstructor<StateInit> = StateInitTlbConstructor
    }
}

private object StateInitTlbConstructor : TlbConstructor<StateInit>(
    schema = "_ split_depth:(Maybe (## 5)) special:(Maybe TickTock) code:(Maybe ^Cell) data:(Maybe ^Cell) library:(HashmapE 256 SimpleLib) = StateInit;"
) {
    private val Maybe5 = Maybe(UIntTlbConstructor.int(5))
    private val MaybeTickTock = Maybe(TickTock)
    private val MaybeCell = Maybe(CellRef.tlbCodec(AnyTlbConstructor))
    private val Library = HashMapE.tlbCodec(256, SimpleLib)

    override fun storeTlb(
        cellBuilder: CellBuilder, value: StateInit
    ) = cellBuilder {
        storeTlb(Maybe5, value.splitDepth)
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
