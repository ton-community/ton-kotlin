package org.ton.hashmap.tlb

import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.hashmap.EmptyHashMapE
import org.ton.hashmap.HashMapE
import org.ton.hashmap.RootHashMapE
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor
import org.ton.tlb.TlbDecoder
import org.ton.tlb.TlbEncoder

object HashMapETlbCombinator : TlbCombinator<HashMapE<Any>>(
        constructors = listOf(EmptyHashMapE.tlbCodec, RootHashMapE.tlbCodec)
) {
    override fun encode(
            cellBuilder: CellBuilder,
            value: HashMapE<Any>,
            typeParam: TlbEncoder<Any>?,
            param: Int,
            negativeParam: ((Int) -> Unit)?
    ) {
        when (value) {
            is RootHashMapE -> RootHashMapETlbConstructor.encode(cellBuilder, value, typeParam, param, negativeParam)
            is EmptyHashMapE -> EmptyHashMapETlbConstructor.encode(cellBuilder, value, typeParam, param, negativeParam)
        }
    }

    override fun decode(
            cellSlice: CellSlice,
            typeParam: TlbDecoder<Any>?,
            param: Int,
            negativeParam: ((Int) -> Unit)?
    ): HashMapE<Any> {
        return if (cellSlice.loadBit()) {
            RootHashMapETlbConstructor.decode(cellSlice, typeParam, param, negativeParam)
        } else {
            EmptyHashMapETlbConstructor.decode(cellSlice, typeParam, param, negativeParam)
        }
    }
}

val HashMapE.Companion.tlbCodec get() = HashMapETlbCombinator

object EmptyHashMapETlbConstructor : TlbConstructor<EmptyHashMapE<Any>>(
        schema = "hme_empty\$0 {n:#} {X:Type} = HashmapE n X;"
) {
    override fun encode(
            cellBuilder: CellBuilder,
            value: EmptyHashMapE<Any>,
            typeParam: TlbEncoder<Any>?,
            param: Int,
            negativeParam: ((Int) -> Unit)?
    ) = Unit

    override fun decode(
            cellSlice: CellSlice,
            typeParam: TlbDecoder<Any>?,
            param: Int,
            negativeParam: ((Int) -> Unit)?
    ): EmptyHashMapE<Any> = EmptyHashMapE()
}

val EmptyHashMapE.Companion.tlbCodec get() = EmptyHashMapETlbConstructor

object RootHashMapETlbConstructor : TlbConstructor<RootHashMapE<Any>>(
        schema = "hme_root\$1 {n:#} {X:Type} root:^(Hashmap n X) = HashmapE n X;"
) {
    override fun encode(
            cellBuilder: CellBuilder,
            value: RootHashMapE<Any>,
            typeParam: TlbEncoder<Any>?,
            param: Int,
            negativeParam: ((Int) -> Unit)?
    ) {
        cellBuilder.storeRef(CellBuilder.createCell {
            HashMapEdgeTlbConstructor.encode(this, value.root, typeParam, param, negativeParam)
        })
    }

    override fun decode(
            cellSlice: CellSlice,
            typeParam: TlbDecoder<Any>?,
            param: Int,
            negativeParam: ((Int) -> Unit)?
    ): RootHashMapE<Any> {
        val root = HashMapEdgeTlbConstructor.decode(cellSlice.loadRef().beginParse(), typeParam, param, negativeParam)
        return RootHashMapE(root)
    }
}

val RootHashMapE.Companion.tlbCodec get() = RootHashMapETlbConstructor
